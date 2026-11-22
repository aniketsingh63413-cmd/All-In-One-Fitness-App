package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.catalog.ExerciseItem
import com.example.data.catalog.MuscleGroup
import com.example.ui.components.CircularMetricRing
import com.example.ui.components.ExerciseDetailSheet
import com.example.ui.components.FeatureCardTile
import com.example.ui.components.featureVisuals
import com.example.ui.components.muscleGroupIcon
import com.example.ui.theme.AccentAiCoach
import com.example.ui.theme.AccentCommunity
import com.example.ui.theme.AccentDiet
import com.example.ui.theme.AccentLearning
import com.example.ui.theme.AccentLibrary
import com.example.ui.theme.AccentNutrition
import com.example.ui.theme.AccentProgress
import com.example.ui.theme.AccentSleep
import com.example.ui.theme.AccentWater
import com.example.ui.theme.AccentWeight
import com.example.ui.theme.AccentWorkout
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.EmeraldBright
import com.example.ui.viewmodel.FitPulseUiState

private data class HomeFeatureSpec(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String?,
    val route: String
)

@Composable
fun HomeScreen(
    state: FitPulseUiState,
    onSearchChange: (String) -> Unit,
    onNavigate: (String) -> Unit,
    onLogFoodQuick: (com.example.data.catalog.FoodItem) -> Unit,
    onLogExerciseSet: (ExerciseItem) -> Unit,
    onToggleBookmarkExercise: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedExercise by remember { mutableStateOf<ExerciseItem?>(null) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val columns = when {
            maxWidth >= 900.dp -> 4
            maxWidth >= 600.dp -> 3
            else -> 2
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 1100.dp)
                .align(Alignment.TopCenter),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Header + Readiness Hero Card
            item {
                HomeHeroDashboardCard(
                    state = state,
                    onOpenAllFeatures = { onNavigate("all_features") },
                    onStartWorkout = { onNavigate("workout") }
                )
            }

            // 2. Prominent Search Bar across exercises, foods, workouts & health topics
            item {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search exercises, foods, workouts, or topics...") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_bar")
                )
            }

            // Live Search Results (when user types)
            if (state.searchQuery.isNotBlank()) {
                item {
                    LiveSearchResultsCard(
                        state = state,
                        onNavigate = onNavigate,
                        onOpenExercise = { selectedExercise = it },
                        onLogFood = onLogFoodQuick
                    )
                }
            }

            // 3. Muscle & Training Grid (Workout + 8 Muscle Groups: Chest, Biceps, Triceps, Shoulder, Back, Legs, Abs, Full Body)
            item {
                SectionHeaderRow(
                    title = "Workouts & Muscle Splits",
                    actionLabel = "All Features",
                    onAction = { onNavigate("all_features") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                val trainingCards = listOf(
                    HomeFeatureSpec("feat_workout", "Workout", "Programs, live timer & log", "${state.workoutLogs.size} logs", "workout")
                ) + MuscleGroup.entries.map { group ->
                    HomeFeatureSpec(
                        id = "feat_${group.name.lowercase()}",
                        title = group.displayName,
                        subtitle = group.tagline,
                        badge = "6 moves",
                        route = "muscle_workout/${group.name}"
                    )
                }
                AdaptiveGridRows(columns = columns, items = trainingCards) { item ->
                    val (icon, color) = if (item.id == "feat_workout") {
                        Icons.Filled.FitnessCenter to AccentWorkout
                    } else {
                        val group = MuscleGroup.entries.first { "feat_${it.name.lowercase()}" == item.id }
                        muscleGroupIcon(group) to group.accentColor
                    }
                    FeatureCardTile(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = icon,
                        accent = color,
                        tag = "home_card_${item.id}",
                        badge = item.badge,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }

            // 4. Diet, Nutrition & Weight Management
            item {
                SectionHeaderRow(title = "Diet, Nutrition & Weight")
                Spacer(modifier = Modifier.height(10.dp))
                val nutritionCards = listOf(
                    HomeFeatureSpec("feat_diet_plan", "Diet Plan", "4 curated daily meal blueprints", "Plans", "diet_plan"),
                    HomeFeatureSpec("feat_nutrition", "Nutrition", "${state.todayCaloriesIn} / ${state.userProfile.dailyCalorieGoal} kcal", "${state.todayProteinG}g P", "nutrition"),
                    HomeFeatureSpec("feat_weight", "Weight Management", "${"%.1f".format(state.userProfile.currentWeightKg)} kg -> ${"%.1f".format(state.userProfile.targetWeightKg)} kg", "BMI/TDEE", "weight_management")
                )
                AdaptiveGridRows(columns = columns, items = nutritionCards) { item ->
                    val (icon, color) = featureVisuals(item.id)
                    FeatureCardTile(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = icon,
                        accent = color,
                        tag = "home_card_${item.id}",
                        badge = item.badge,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }

            // 5. Daily Trackers & Analytics
            item {
                SectionHeaderRow(title = "Trackers & Analytics")
                Spacer(modifier = Modifier.height(10.dp))
                val trackerCards = listOf(
                    HomeFeatureSpec("feat_progress", "Progress Tracker", "Volume charts, calories & PRs", "${state.workoutLogs.size} total", "progress_tracker"),
                    HomeFeatureSpec("feat_water", "Water Tracker", "${state.todayWaterMl} / ${state.userProfile.dailyWaterGoalMl} ml", "Hydrate", "water_tracker"),
                    HomeFeatureSpec("feat_sleep", "Sleep Tracker", state.latestSleep?.let { "${"%.1f".format(it.hoursSlept)} hrs • ${it.qualityRating}/5★" } ?: "Log sleep & recovery", "Recovery", "sleep_tracker")
                )
                AdaptiveGridRows(columns = columns, items = trackerCards) { item ->
                    val (icon, color) = featureVisuals(item.id)
                    FeatureCardTile(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = icon,
                        accent = color,
                        tag = "home_card_${item.id}",
                        badge = item.badge,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }

            // 6. AI Coach, Exercise Library, Learning Center & Community
            item {
                SectionHeaderRow(title = "Coaching, Knowledge & Community")
                Spacer(modifier = Modifier.height(10.dp))
                val knowledgeCards = listOf(
                    HomeFeatureSpec("feat_ai_coach", "AI Fitness Coach", "Workouts, macros & recovery Q&A", "Coach", "ai_coach"),
                    HomeFeatureSpec("feat_library", "Exercise Library", "48+ guided moves & form cues", "48+ moves", "exercise_library"),
                    HomeFeatureSpec("feat_learning", "Learning Center", "Evidence-based fitness articles", "6 guides", "learning_center"),
                    HomeFeatureSpec("feat_community", "Community", "Milestones, Q&A & athlete feed", "${state.communityPosts.size} posts", "community")
                )
                AdaptiveGridRows(columns = columns, items = knowledgeCards) { item ->
                    val (icon, color) = featureVisuals(item.id)
                    FeatureCardTile(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = icon,
                        accent = color,
                        tag = "home_card_${item.id}",
                        badge = item.badge,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }

            // 7. Dedicated All Features Banner Button
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onNavigate("all_features") }
                        .testTag("home_all_features_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.GridView,
                                    contentDescription = "All Features",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Column {
                                Text(
                                    text = "All Features Directory",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Browse every workout split, tracker & tool in one list",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Open All Features",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }

    selectedExercise?.let { exercise ->
        ExerciseDetailSheet(
            exercise = exercise,
            isBookmarked = state.bookmarkedExerciseIds.contains(exercise.id),
            onToggleBookmark = { onToggleBookmarkExercise(exercise.id) },
            onLogExerciseSet = { onLogExerciseSet(exercise) },
            onDismiss = { selectedExercise = null }
        )
    }
}

@Composable
private fun HomeHeroDashboardCard(
    state: FitPulseUiState,
    onOpenAllFeatures: () -> Unit,
    onStartWorkout: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0E1A1E),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_workout),
                contentDescription = "FitPulse athletic banner",
                contentScale = ContentScale.Crop,
                alpha = 0.38f,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0A1416).copy(alpha = 0.94f),
                                Color(0xFF0A1416).copy(alpha = 0.72f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "FITPULSE DASHBOARD",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Welcome back, ${state.userProfile.name}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Text(
                            text = state.userProfile.fitnessGoal,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFB8C9C4)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = EmeraldBright.copy(alpha = 0.16f),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onOpenAllFeatures)
                            .testTag("hero_all_features_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.GridView,
                                contentDescription = "All Features",
                                tint = EmeraldBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "All 20",
                                style = MaterialTheme.typography.labelMedium,
                                color = EmeraldBright
                            )
                        }
                    }
                }

                // Live Today Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val calProgress = state.todayCaloriesIn.toFloat() / state.userProfile.dailyCalorieGoal.coerceAtLeast(1)
                    val waterProgress = state.todayWaterMl.toFloat() / state.userProfile.dailyWaterGoalMl.coerceAtLeast(1)
                    val sleepProgress = (state.latestSleep?.hoursSlept ?: 0f) / state.userProfile.dailySleepGoalHours.coerceAtLeast(1f)

                    HeroRingStat(
                        progress = calProgress,
                        color = CoralAccent,
                        value = "${state.todayCaloriesIn}",
                        label = "kcal in"
                    )
                    HeroRingStat(
                        progress = waterProgress,
                        color = AccentWater,
                        value = "${state.todayWaterMl}ml",
                        label = "water"
                    )
                    HeroRingStat(
                        progress = sleepProgress,
                        color = Color(0xFFA78BFA),
                        value = state.latestSleep?.let { "${"%.1f".format(it.hoursSlept)}h" } ?: "--",
                        label = "sleep"
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = CoralAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${state.todayCaloriesBurned} kcal",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "burned today",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA0B3AE)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = onStartWorkout,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldBright,
                                contentColor = Color(0xFF00291A)
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("hero_start_workout_button")
                        ) {
                            Text("Train Now", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroRingStat(
    progress: Float,
    color: Color,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF142226)
        ) {
            Box(modifier = Modifier.padding(2.dp)) {
                CircularMetricRing(
                    progress = progress,
                    color = color,
                    size = 56.dp,
                    strokeWidth = 5.dp,
                    centerLabel = value,
                    centerSubLabel = null
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color(0xFFB8C9C4))
    }
}

@Composable
private fun LiveSearchResultsCard(
    state: FitPulseUiState,
    onNavigate: (String) -> Unit,
    onOpenExercise: (ExerciseItem) -> Unit,
    onLogFood: (com.example.data.catalog.FoodItem) -> Unit
) {
    val results = state.searchResults
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Search Results for \"${state.searchQuery}\"",
                style = MaterialTheme.typography.titleMedium
            )
            if (results.isEmpty) {
                Text(
                    text = "No matching exercises, foods, workouts, or topics found. Try \"chest\", \"salmon\", \"squat\", or \"sleep\".",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                results.features.take(4).forEach { feat ->
                    SearchRowItem(
                        tagLabel = "FEATURE",
                        title = feat.title,
                        subtitle = feat.subtitle,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = { onNavigate(feat.route) }
                    )
                }
                results.exercises.take(5).forEach { ex ->
                    SearchRowItem(
                        tagLabel = ex.muscleGroup.displayName.uppercase(),
                        title = ex.name,
                        subtitle = "${ex.equipment} • ${ex.defaultSets}x${ex.defaultReps}",
                        color = ex.muscleGroup.accentColor,
                        onClick = { onOpenExercise(ex) }
                    )
                }
                results.programs.take(3).forEach { prog ->
                    SearchRowItem(
                        tagLabel = "WORKOUT",
                        title = prog.title,
                        subtitle = "${prog.durationMinutes} min • ${prog.level}",
                        color = AccentWorkout,
                        onClick = { onNavigate("active_workout/${prog.id}") }
                    )
                }
                results.foods.take(4).forEach { food ->
                    SearchRowItem(
                        tagLabel = "FOOD (+LOG)",
                        title = food.name,
                        subtitle = "${food.calories} kcal • ${food.proteinG}g P • ${food.carbsG}g C",
                        color = AccentNutrition,
                        onClick = {
                            onLogFood(food)
                            onNavigate("nutrition")
                        }
                    )
                }
                results.articles.take(3).forEach { art ->
                    SearchRowItem(
                        tagLabel = "TOPIC",
                        title = art.title,
                        subtitle = "${art.category} • ${art.readMinutes} min read",
                        color = AccentLearning,
                        onClick = { onNavigate("article_detail/${art.id}") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchRowItem(
    tagLabel: String,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tagLabel, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun SectionHeaderRow(
    title: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        if (actionLabel != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
fun <T> AdaptiveGridRows(
    columns: Int,
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    val rows = items.chunked(columns)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        itemContent(item)
                    }
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
