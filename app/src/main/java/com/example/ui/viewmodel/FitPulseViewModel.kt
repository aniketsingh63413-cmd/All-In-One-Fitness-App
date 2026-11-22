package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.AiRepository
import com.example.data.ai.CoachUserContext
import com.example.data.ai.PlaceholderAiCoachRepository
import com.example.data.catalog.AppFeatureDescriptor
import com.example.data.catalog.DietPlanMeal
import com.example.data.catalog.ExerciseItem
import com.example.data.catalog.FoodItem
import com.example.data.catalog.LearningArticle
import com.example.data.catalog.MuscleGroup
import com.example.data.catalog.WellnessCatalog
import com.example.data.catalog.WorkoutCatalog
import com.example.data.catalog.WorkoutProgram
import com.example.data.local.BookmarkEntity
import com.example.data.local.CommunityPostEntity
import com.example.data.local.FitPulseDatabase
import com.example.data.local.MealLogEntity
import com.example.data.local.SleepLogEntity
import com.example.data.local.UserPreferencesRepository
import com.example.data.local.UserProfile
import com.example.data.local.WaterLogEntity
import com.example.data.local.WeightLogEntity
import com.example.data.local.WorkoutLogEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class AiChatMessage(
    val id: Long,
    val isUser: Boolean,
    val text: String,
    val isSampleResponse: Boolean = false,
    val suggestedActionLabel: String? = null,
    val suggestedRoute: String? = null
)

data class ActiveWorkoutState(
    val program: WorkoutProgram? = null,
    val exercises: List<ExerciseItem> = emptyList(),
    val completedSets: Map<String, Int> = emptyMap(), // exerciseId -> sets completed
    val elapsedSeconds: Int = 0,
    val restSecondsRemaining: Int = 0,
    val isRestRunning: Boolean = false,
    val isWorkoutSaved: Boolean = false
)

data class SearchResults(
    val features: List<AppFeatureDescriptor> = emptyList(),
    val exercises: List<ExerciseItem> = emptyList(),
    val programs: List<WorkoutProgram> = emptyList(),
    val foods: List<FoodItem> = emptyList(),
    val articles: List<LearningArticle> = emptyList()
) {
    val isEmpty: Boolean
        get() = features.isEmpty() && exercises.isEmpty() && programs.isEmpty() && foods.isEmpty() && articles.isEmpty()
}

data class FitPulseUiState(
    val userProfile: UserProfile = UserProfile(),
    val workoutLogs: List<WorkoutLogEntity> = emptyList(),
    val mealLogs: List<MealLogEntity> = emptyList(),
    val weightLogs: List<WeightLogEntity> = emptyList(),
    val waterLogs: List<WaterLogEntity> = emptyList(),
    val sleepLogs: List<SleepLogEntity> = emptyList(),
    val communityPosts: List<CommunityPostEntity> = emptyList(),
    val bookmarkedExerciseIds: Set<String> = emptySet(),
    val bookmarkedArticleIds: Set<String> = emptySet(),
    val todayWaterMl: Int = 0,
    val todayCaloriesIn: Int = 0,
    val todayProteinG: Int = 0,
    val todayCarbsG: Int = 0,
    val todayFatsG: Int = 0,
    val todayCaloriesBurned: Int = 0,
    val latestSleep: SleepLogEntity? = null,
    val searchQuery: String = "",
    val searchResults: SearchResults = SearchResults(),
    val aiMessages: List<AiChatMessage> = emptyList(),
    val isAiThinking: Boolean = false,
    val isLiveGeminiConnected: Boolean = false,
    val activeWorkout: ActiveWorkoutState = ActiveWorkoutState()
)

private data class LocalDbSnapshot(
    val workouts: List<WorkoutLogEntity>,
    val meals: List<MealLogEntity>,
    val weights: List<WeightLogEntity>,
    val water: List<WaterLogEntity>,
    val sleep: List<SleepLogEntity>,
    val posts: List<CommunityPostEntity>,
    val bookmarks: List<BookmarkEntity>
)

class FitPulseViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FitPulseDatabase.getInstance(application)
    private val prefsRepo = UserPreferencesRepository(application)
    private val aiRepository: AiRepository = PlaceholderAiCoachRepository()

    private val _searchQuery = MutableStateFlow("")
    private val _aiMessages = MutableStateFlow(
        listOf(
            AiChatMessage(
                id = 1L,
                isUser = false,
                text = "[Sample AI Coach Mode]\nWelcome to FitPulse Coach! Ask me for a Chest, Arms, Back, Legs, Abs, or Full Body workout, macro targets, weight management advice, or sleep recovery tips.",
                isSampleResponse = true,
                suggestedActionLabel = "Open Workout Hub",
                suggestedRoute = "workout"
            )
        )
    )
    private val _isAiThinking = MutableStateFlow(false)
    private val _activeWorkout = MutableStateFlow(ActiveWorkoutState())

    private var workoutTickerJob: Job? = null
    private var restTimerJob: Job? = null

    private val dbFlow = combine(
        db.workoutLogDao().observeAll(),
        db.mealLogDao().observeAll(),
        db.weightLogDao().observeAll(),
        db.waterLogDao().observeAll(),
        db.sleepLogDao().observeAll(),
    ) { workouts, meals, weights, water, sleep ->
        Triple(workouts, meals, Triple(weights, water, sleep))
    }.combine(db.communityPostDao().observeAll()) { first, posts ->
        Pair(first, posts)
    }.combine(db.bookmarkDao().observeAll()) { pair, bookmarks ->
        val (first, posts) = pair
        val (workouts, meals, triple) = first
        val (weights, water, sleep) = triple
        LocalDbSnapshot(workouts, meals, weights, water, sleep, posts, bookmarks)
    }

    val uiState: StateFlow<FitPulseUiState> = combine(
        prefsRepo.userProfile,
        dbFlow,
        _searchQuery,
        _aiMessages,
        _isAiThinking
    ) { profile, snap, query, aiMsgs, aiThinking ->
        Pair(profile, snap) to Triple(query, aiMsgs, aiThinking)
    }.combine(_activeWorkout) { combined, activeWorkout ->
        val (profileAndSnap, triple) = combined
        val (profile, snap) = profileAndSnap
        val (query, aiMsgs, aiThinking) = triple

        val startOfToday = startOfTodayMillis()
        val todayWater = snap.water.filter { it.timestamp >= startOfToday }.sumOf { it.amountMl }
        val todayMeals = snap.meals.filter { it.timestamp >= startOfToday }
        val todayWorkouts = snap.workouts.filter { it.timestamp >= startOfToday }

        FitPulseUiState(
            userProfile = profile,
            workoutLogs = snap.workouts,
            mealLogs = snap.meals,
            weightLogs = snap.weights,
            waterLogs = snap.water,
            sleepLogs = snap.sleep,
            communityPosts = snap.posts,
            bookmarkedExerciseIds = snap.bookmarks.filter { it.itemType == "EXERCISE" }.map { it.itemId }.toSet(),
            bookmarkedArticleIds = snap.bookmarks.filter { it.itemType == "ARTICLE" }.map { it.itemId }.toSet(),
            todayWaterMl = todayWater,
            todayCaloriesIn = todayMeals.sumOf { it.calories },
            todayProteinG = todayMeals.sumOf { it.proteinG },
            todayCarbsG = todayMeals.sumOf { it.carbsG },
            todayFatsG = todayMeals.sumOf { it.fatsG },
            todayCaloriesBurned = todayWorkouts.sumOf { it.caloriesBurned },
            latestSleep = snap.sleep.firstOrNull(),
            searchQuery = query,
            searchResults = performSearch(query),
            aiMessages = aiMsgs,
            isAiThinking = aiThinking,
            isLiveGeminiConnected = aiRepository.isLiveGeminiConnected,
            activeWorkout = activeWorkout
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FitPulseUiState()
    )

    private fun startOfTodayMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun performSearch(rawQuery: String): SearchResults {
        val q = rawQuery.trim().lowercase()
        if (q.isBlank()) return SearchResults()

        return SearchResults(
            features = WellnessCatalog.allFeatures.filter {
                it.title.lowercase().contains(q) ||
                    it.subtitle.lowercase().contains(q) ||
                    it.category.lowercase().contains(q)
            },
            exercises = WorkoutCatalog.exercises.filter {
                it.name.lowercase().contains(q) ||
                    it.muscleGroup.displayName.lowercase().contains(q) ||
                    it.equipment.lowercase().contains(q) ||
                    it.secondaryMuscles.lowercase().contains(q)
            },
            programs = WorkoutCatalog.programs.filter {
                it.title.lowercase().contains(q) ||
                    it.muscleGroup.displayName.lowercase().contains(q) ||
                    it.description.lowercase().contains(q)
            },
            foods = WellnessCatalog.foods.filter {
                it.name.lowercase().contains(q) ||
                    it.mealType.lowercase().contains(q) ||
                    it.tags.lowercase().contains(q)
            },
            articles = WellnessCatalog.articles.filter {
                it.title.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.summary.lowercase().contains(q)
            }
        )
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Water Tracker
    fun addWater(amountMl: Int) {
        if (amountMl <= 0) return
        viewModelScope.launch {
            db.waterLogDao().insert(WaterLogEntity(amountMl = amountMl))
        }
    }

    fun deleteWaterLog(id: Long) {
        viewModelScope.launch {
            db.waterLogDao().deleteById(id)
        }
    }

    fun updateWaterGoal(goalMl: Int) {
        viewModelScope.launch {
            prefsRepo.updateWaterGoal(goalMl)
        }
    }

    // Sleep Tracker
    fun addSleepLog(hours: Float, quality: Int, bedtime: String, wakeTime: String, note: String) {
        viewModelScope.launch {
            db.sleepLogDao().insert(
                SleepLogEntity(
                    hoursSlept = hours.coerceIn(1f, 16f),
                    qualityRating = quality.coerceIn(1, 5),
                    bedtime = bedtime.ifBlank { "10:30 PM" },
                    wakeTime = wakeTime.ifBlank { "6:30 AM" },
                    note = note.trim()
                )
            )
        }
    }

    fun deleteSleepLog(id: Long) {
        viewModelScope.launch {
            db.sleepLogDao().deleteById(id)
        }
    }

    fun updateSleepGoal(goalHours: Float) {
        viewModelScope.launch {
            prefsRepo.updateSleepGoal(goalHours)
        }
    }

    // Nutrition & Diet Plan
    fun logFoodItem(food: FoodItem, mealTypeOverride: String? = null) {
        viewModelScope.launch {
            db.mealLogDao().insert(
                MealLogEntity(
                    name = food.name,
                    mealType = mealTypeOverride ?: food.mealType,
                    calories = food.calories,
                    proteinG = food.proteinG,
                    carbsG = food.carbsG,
                    fatsG = food.fatsG,
                    servingInfo = food.servingInfo
                )
            )
        }
    }

    fun logDietPlanMeal(meal: DietPlanMeal) {
        viewModelScope.launch {
            db.mealLogDao().insert(
                MealLogEntity(
                    name = meal.title,
                    mealType = meal.mealType,
                    calories = meal.calories,
                    proteinG = meal.proteinG,
                    carbsG = meal.carbsG,
                    fatsG = meal.fatsG,
                    servingInfo = meal.itemsSummary
                )
            )
        }
    }

    fun logCustomMeal(name: String, mealType: String, calories: Int, protein: Int, carbs: Int, fats: Int) {
        if (name.isBlank() || calories <= 0) return
        viewModelScope.launch {
            db.mealLogDao().insert(
                MealLogEntity(
                    name = name.trim(),
                    mealType = mealType,
                    calories = calories,
                    proteinG = protein.coerceAtLeast(0),
                    carbsG = carbs.coerceAtLeast(0),
                    fatsG = fats.coerceAtLeast(0),
                    servingInfo = "Custom entry"
                )
            )
        }
    }

    fun deleteMealLog(id: Long) {
        viewModelScope.launch {
            db.mealLogDao().deleteById(id)
        }
    }

    // Weight Management
    fun addWeightLog(weightKg: Float, bodyFatPercent: Float?, note: String) {
        if (weightKg <= 20f || weightKg > 350f) return
        viewModelScope.launch {
            db.weightLogDao().insert(
                WeightLogEntity(
                    weightKg = weightKg,
                    bodyFatPercent = bodyFatPercent,
                    note = note.trim()
                )
            )
            prefsRepo.updateCurrentWeight(weightKg)
        }
    }

    fun deleteWeightLog(id: Long) {
        viewModelScope.launch {
            db.weightLogDao().deleteById(id)
        }
    }

    // Workouts & Active Session Player
    fun logQuickWorkout(
        title: String,
        muscleGroup: MuscleGroup,
        durationMinutes: Int,
        caloriesBurned: Int,
        setsCompleted: Int,
        volumeKg: Int = setsCompleted * 240,
        notes: String = ""
    ) {
        viewModelScope.launch {
            db.workoutLogDao().insert(
                WorkoutLogEntity(
                    title = title,
                    muscleGroup = muscleGroup.displayName,
                    durationMinutes = durationMinutes.coerceAtLeast(5),
                    caloriesBurned = caloriesBurned.coerceAtLeast(25),
                    setsCompleted = setsCompleted.coerceAtLeast(1),
                    volumeKg = volumeKg.coerceAtLeast(0),
                    notes = notes
                )
            )
        }
    }

    fun deleteWorkoutLog(id: Long) {
        viewModelScope.launch {
            db.workoutLogDao().deleteById(id)
        }
    }

    fun startWorkoutProgram(programId: String) {
        val program = WorkoutCatalog.findProgram(programId) ?: return
        val exercises = program.exerciseIds.mapNotNull { WorkoutCatalog.findExercise(it) }
        _activeWorkout.value = ActiveWorkoutState(
            program = program,
            exercises = exercises,
            completedSets = exercises.associate { it.id to 0 },
            elapsedSeconds = 0,
            restSecondsRemaining = 0,
            isRestRunning = false,
            isWorkoutSaved = false
        )
        workoutTickerJob?.cancel()
        workoutTickerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _activeWorkout.update { state ->
                    if (state.program != null && !state.isWorkoutSaved) {
                        state.copy(elapsedSeconds = state.elapsedSeconds + 1)
                    } else state
                }
            }
        }
    }

    fun toggleExerciseSet(exerciseId: String, targetSets: Int, restSeconds: Int) {
        var shouldStartRest = false
        _activeWorkout.update { state ->
            val current = state.completedSets[exerciseId] ?: 0
            val next = if (current >= targetSets) 0 else current + 1
            if (next > current) {
                shouldStartRest = true
            }
            state.copy(
                completedSets = state.completedSets.toMutableMap().apply { put(exerciseId, next) }
            )
        }
        if (shouldStartRest) {
            startRestTimer(restSeconds)
        }
    }

    fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _activeWorkout.update { it.copy(restSecondsRemaining = seconds, isRestRunning = true) }
        restTimerJob = viewModelScope.launch {
            while (_activeWorkout.value.restSecondsRemaining > 0) {
                delay(1000L)
                _activeWorkout.update { s ->
                    val next = (s.restSecondsRemaining - 1).coerceAtLeast(0)
                    s.copy(restSecondsRemaining = next, isRestRunning = next > 0)
                }
            }
        }
    }

    fun skipRestTimer() {
        restTimerJob?.cancel()
        _activeWorkout.update { it.copy(restSecondsRemaining = 0, isRestRunning = false) }
    }

    fun finishAndSaveActiveWorkout() {
        val state = _activeWorkout.value
        val program = state.program ?: return
        val totalSets = state.completedSets.values.sum().coerceAtLeast(1)
        val durationMin = (state.elapsedSeconds / 60).coerceAtLeast(program.durationMinutes.coerceAtMost(15))
        val estimatedCals = state.exercises.sumOf { ex ->
            val setsDone = state.completedSets[ex.id] ?: 0
            setsDone * ex.caloriesPerSet
        }.coerceAtLeast(80)

        viewModelScope.launch {
            db.workoutLogDao().insert(
                WorkoutLogEntity(
                    title = program.title,
                    muscleGroup = program.muscleGroup.displayName,
                    durationMinutes = durationMin,
                    caloriesBurned = estimatedCals,
                    setsCompleted = totalSets,
                    volumeKg = totalSets * 280,
                    notes = "Guided session completed"
                )
            )
            workoutTickerJob?.cancel()
            restTimerJob?.cancel()
            _activeWorkout.update { it.copy(isWorkoutSaved = true, isRestRunning = false) }
        }
    }

    // Bookmarks
    fun toggleBookmark(itemId: String, itemType: String) {
        viewModelScope.launch {
            val current = uiState.value
            val isBookmarked = if (itemType == "EXERCISE") {
                current.bookmarkedExerciseIds.contains(itemId)
            } else {
                current.bookmarkedArticleIds.contains(itemId)
            }
            if (isBookmarked) {
                db.bookmarkDao().delete(itemId, itemType)
            } else {
                db.bookmarkDao().insert(BookmarkEntity(itemId = itemId, itemType = itemType))
            }
        }
    }

    // Community
    fun createCommunityPost(category: String, title: String, body: String) {
        if (title.isBlank() || body.isBlank()) return
        val profile = uiState.value.userProfile
        val handle = "@" + profile.name.lowercase().replace(" ", "_")
        viewModelScope.launch {
            db.communityPostDao().insert(
                CommunityPostEntity(
                    authorName = profile.name,
                    authorHandle = handle,
                    category = category,
                    title = title.trim(),
                    body = body.trim(),
                    likesCount = 1,
                    isLiked = true
                )
            )
        }
    }

    fun togglePostLike(post: CommunityPostEntity) {
        viewModelScope.launch {
            val nextLiked = !post.isLiked
            val nextCount = if (nextLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
            db.communityPostDao().updateLike(post.id, nextLiked, nextCount)
        }
    }

    fun togglePostBookmark(post: CommunityPostEntity) {
        viewModelScope.launch {
            db.communityPostDao().updateBookmark(post.id, !post.isBookmarked)
        }
    }

    fun addPostComment(post: CommunityPostEntity, comment: String) {
        if (comment.isBlank()) return
        val author = uiState.value.userProfile.name
        val entry = "$author: ${comment.trim()}"
        val updated = if (post.commentsText.isBlank()) entry else "${post.commentsText}\n$entry"
        viewModelScope.launch {
            db.communityPostDao().updateComments(post.id, updated)
        }
    }

    // AI Fitness Coach
    fun sendAiPrompt(prompt: String) {
        val trimmed = prompt.trim()
        if (trimmed.isEmpty() || _isAiThinking.value) return

        val userMsg = AiChatMessage(
            id = System.currentTimeMillis(),
            isUser = true,
            text = trimmed
        )
        _aiMessages.update { it + userMsg }
        _isAiThinking.value = true

        viewModelScope.launch {
            val snap = uiState.value
            val ctx = CoachUserContext(
                userName = snap.userProfile.name,
                fitnessGoal = snap.userProfile.fitnessGoal,
                currentWeightKg = snap.userProfile.currentWeightKg,
                targetWeightKg = snap.userProfile.targetWeightKg,
                caloriesToday = snap.todayCaloriesIn,
                calorieGoal = snap.userProfile.dailyCalorieGoal,
                proteinTodayG = snap.todayProteinG,
                proteinGoalG = snap.userProfile.dailyProteinGoalG,
                waterTodayMl = snap.todayWaterMl,
                waterGoalMl = snap.userProfile.dailyWaterGoalMl,
                lastSleepHours = snap.latestSleep?.hoursSlept,
                workoutsCompletedTotal = snap.workoutLogs.size
            )
            val reply = aiRepository.askCoach(trimmed, ctx)
            _aiMessages.update {
                it + AiChatMessage(
                    id = System.currentTimeMillis() + 1L,
                    isUser = false,
                    text = reply.text,
                    isSampleResponse = reply.isSampleResponse,
                    suggestedActionLabel = reply.suggestedActionLabel,
                    suggestedRoute = reply.suggestedRoute
                )
            }
            _isAiThinking.value = false
        }
    }

    fun clearAiChat() {
        _aiMessages.value = listOf(
            AiChatMessage(
                id = System.currentTimeMillis(),
                isUser = false,
                text = "[Sample AI Coach Mode]\nFresh session started! Ask me about muscle splits, calorie/protein plans, weight management, or recovery.",
                isSampleResponse = true
            )
        )
    }

    // Profile
    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            prefsRepo.updateProfile(profile)
        }
    }

    override fun onCleared() {
        super.onCleared()
        workoutTickerJob?.cancel()
        restTimerJob?.cancel()
    }
}
