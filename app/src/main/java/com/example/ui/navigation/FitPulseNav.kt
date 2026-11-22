package com.example.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.catalog.MuscleGroup
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.AiCoachScreen
import com.example.ui.screens.AllFeaturesScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.DietHubScreen
import com.example.ui.screens.DietPlanScreen
import com.example.ui.screens.ExerciseLibraryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearningCenterScreen
import com.example.ui.screens.MuscleWorkoutScreen
import com.example.ui.screens.NutritionScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressTrackerScreen
import com.example.ui.screens.SleepTrackerScreen
import com.example.ui.screens.WaterTrackerScreen
import com.example.ui.screens.WeightManagementScreen
import com.example.ui.screens.WorkoutHubScreen
import com.example.ui.theme.FitPulseTheme
import com.example.ui.viewmodel.FitPulseViewModel

object Routes {
    const val HOME = "home"
    const val WORKOUT = "workout"
    const val DIET = "diet"
    const val AI_COACH = "ai_coach"
    const val PROFILE = "profile"
    const val ALL_FEATURES = "all_features"
    const val MUSCLE_WORKOUT = "muscle_workout/{muscleGroup}"
    const val ACTIVE_WORKOUT = "active_workout/{programId}"
    const val DIET_PLAN = "diet_plan"
    const val NUTRITION = "nutrition"
    const val WEIGHT_MANAGEMENT = "weight_management"
    const val EXERCISE_LIBRARY = "exercise_library"
    const val PROGRESS_TRACKER = "progress_tracker"
    const val WATER_TRACKER = "water_tracker"
    const val SLEEP_TRACKER = "sleep_tracker"
    const val LEARNING_CENTER = "learning_center"
    const val ARTICLE_DETAIL = "article_detail/{articleId}"
    const val COMMUNITY = "community"
}

private data class TopLevelTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val topLevelTabs = listOf(
    TopLevelTab(Routes.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    TopLevelTab(Routes.WORKOUT, "Workout", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    TopLevelTab(Routes.DIET, "Diet", Icons.Filled.Restaurant, Icons.Outlined.Restaurant),
    TopLevelTab(Routes.AI_COACH, "AI Coach", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    TopLevelTab(Routes.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun FitPulseApp(viewModel: FitPulseViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val useDark = state.userProfile.darkThemeOverride ?: isSystemInDarkTheme()

    FitPulseTheme(darkTheme = useDark) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val topRoutesSet = topLevelTabs.map { it.route }.toSet()
        val isTopLevel = currentDestination?.route in topRoutesSet

        fun navigateTopLevel(route: String) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        fun navigateRoute(route: String) {
            if (route in topRoutesSet) {
                navigateTopLevel(route)
            } else {
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useNavRail = maxWidth >= 600.dp

            if (useNavRail) {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (isTopLevel) {
                            NavigationRail(modifier = Modifier.fillMaxHeight()) {
                                topLevelTabs.forEach { tab ->
                                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                                    NavigationRailItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(tab.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.label
                                            )
                                        },
                                        label = { Text(tab.label) },
                                        modifier = Modifier.testTag("nav_rail_${tab.route}")
                                    )
                                }
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            FitPulseNavHost(
                                navController = navController,
                                viewModel = viewModel,
                                state = state,
                                onNavigate = ::navigateRoute
                            )
                        }
                    }
                }
            } else {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        if (isTopLevel) {
                            NavigationBar(
                                windowInsets = WindowInsets.navigationBars
                            ) {
                                topLevelTabs.forEach { tab ->
                                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(tab.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.label
                                            )
                                        },
                                        label = { Text(tab.label) },
                                        modifier = Modifier.testTag("bottom_nav_${tab.route}")
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        FitPulseNavHost(
                            navController = navController,
                            viewModel = viewModel,
                            state = state,
                            onNavigate = ::navigateRoute
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FitPulseNavHost(
    navController: NavHostController,
    viewModel: FitPulseViewModel,
    state: com.example.ui.viewmodel.FitPulseUiState,
    onNavigate: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                state = state,
                onSearchChange = viewModel::updateSearchQuery,
                onNavigate = onNavigate,
                onLogFoodQuick = { viewModel.logFoodItem(it) },
                onLogExerciseSet = { ex ->
                    viewModel.logQuickWorkout(
                        title = ex.name,
                        muscleGroup = ex.muscleGroup,
                        durationMinutes = 12,
                        caloriesBurned = ex.defaultSets * ex.caloriesPerSet,
                        setsCompleted = ex.defaultSets
                    )
                },
                onToggleBookmarkExercise = { id -> viewModel.toggleBookmark(id, "EXERCISE") }
            )
        }

        composable(Routes.WORKOUT) {
            WorkoutHubScreen(
                state = state,
                onNavigate = onNavigate,
                onLogCustomWorkout = { title, group, dur, cals, sets ->
                    viewModel.logQuickWorkout(title, group, dur, cals, sets)
                },
                onDeleteWorkoutLog = viewModel::deleteWorkoutLog
            )
        }

        composable(Routes.DIET) {
            DietHubScreen(
                state = state,
                onNavigate = onNavigate,
                onLogFood = { viewModel.logFoodItem(it) }
            )
        }

        composable(Routes.AI_COACH) {
            AiCoachScreen(
                state = state,
                onSendPrompt = viewModel::sendAiPrompt,
                onClearChat = viewModel::clearAiChat,
                onNavigate = onNavigate
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                state = state,
                onSaveProfile = viewModel::saveProfile,
                onNavigate = onNavigate
            )
        }

        composable(Routes.ALL_FEATURES) {
            AllFeaturesScreen(
                onBack = { navController.popBackStack() },
                onNavigate = onNavigate
            )
        }

        composable(
            route = Routes.MUSCLE_WORKOUT,
            arguments = listOf(navArgument("muscleGroup") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupArg = backStackEntry.arguments?.getString("muscleGroup")
            val group = MuscleGroup.entries.find { it.name == groupArg } ?: MuscleGroup.CHEST
            MuscleWorkoutScreen(
                muscleGroup = group,
                state = state,
                onBack = { navController.popBackStack() },
                onStartProgram = { progId -> onNavigate("active_workout/$progId") },
                onQuickLogMuscleSession = {
                    viewModel.logQuickWorkout(
                        title = "${group.displayName} Split Session",
                        muscleGroup = group,
                        durationMinutes = 35,
                        caloriesBurned = 240,
                        setsCompleted = 12
                    )
                },
                onToggleBookmark = { id -> viewModel.toggleBookmark(id, "EXERCISE") },
                onLogExerciseSet = { ex ->
                    viewModel.logQuickWorkout(
                        title = ex.name,
                        muscleGroup = ex.muscleGroup,
                        durationMinutes = 12,
                        caloriesBurned = ex.defaultSets * ex.caloriesPerSet,
                        setsCompleted = ex.defaultSets
                    )
                }
            )
        }

        composable(
            route = Routes.ACTIVE_WORKOUT,
            arguments = listOf(navArgument("programId") { type = NavType.StringType })
        ) { backStackEntry ->
            val programId = backStackEntry.arguments?.getString("programId") ?: ""
            ActiveWorkoutScreen(
                programId = programId,
                activeState = state.activeWorkout,
                onStartProgram = viewModel::startWorkoutProgram,
                onToggleSet = viewModel::toggleExerciseSet,
                onStartRest = viewModel::startRestTimer,
                onSkipRest = viewModel::skipRestTimer,
                onFinishWorkout = viewModel::finishAndSaveActiveWorkout,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DIET_PLAN) {
            DietPlanScreen(
                onBack = { navController.popBackStack() },
                onLogPlanMeal = viewModel::logDietPlanMeal,
                onOpenNutrition = { onNavigate(Routes.NUTRITION) }
            )
        }

        composable(Routes.NUTRITION) {
            NutritionScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onLogFood = { viewModel.logFoodItem(it) },
                onLogCustomMeal = viewModel::logCustomMeal,
                onDeleteMeal = viewModel::deleteMealLog
            )
        }

        composable(Routes.WEIGHT_MANAGEMENT) {
            WeightManagementScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onAddWeight = viewModel::addWeightLog,
                onDeleteWeight = viewModel::deleteWeightLog
            )
        }

        composable(Routes.EXERCISE_LIBRARY) {
            ExerciseLibraryScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onToggleBookmark = { id -> viewModel.toggleBookmark(id, "EXERCISE") },
                onLogExerciseSet = { ex ->
                    viewModel.logQuickWorkout(
                        title = ex.name,
                        muscleGroup = ex.muscleGroup,
                        durationMinutes = 12,
                        caloriesBurned = ex.defaultSets * ex.caloriesPerSet,
                        setsCompleted = ex.defaultSets
                    )
                }
            )
        }

        composable(Routes.PROGRESS_TRACKER) {
            ProgressTrackerScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onNavigateWorkout = { onNavigate(Routes.WORKOUT) }
            )
        }

        composable(Routes.WATER_TRACKER) {
            WaterTrackerScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onAddWater = viewModel::addWater,
                onDeleteWater = viewModel::deleteWaterLog,
                onUpdateGoal = viewModel::updateWaterGoal
            )
        }

        composable(Routes.SLEEP_TRACKER) {
            SleepTrackerScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onAddSleep = viewModel::addSleepLog,
                onDeleteSleep = viewModel::deleteSleepLog
            )
        }

        composable(Routes.LEARNING_CENTER) {
            LearningCenterScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onOpenArticle = { id -> onNavigate("article_detail/$id") },
                onToggleBookmark = { id -> viewModel.toggleBookmark(id, "ARTICLE") }
            )
        }

        composable(
            route = Routes.ARTICLE_DETAIL,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
            ArticleDetailScreen(
                articleId = articleId,
                isBookmarked = state.bookmarkedArticleIds.contains(articleId),
                onToggleBookmark = { viewModel.toggleBookmark(articleId, "ARTICLE") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.COMMUNITY) {
            CommunityScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onCreatePost = viewModel::createCommunityPost,
                onToggleLike = viewModel::togglePostLike,
                onToggleBookmark = viewModel::togglePostBookmark,
                onAddComment = viewModel::addPostComment
            )
        }
    }
}
