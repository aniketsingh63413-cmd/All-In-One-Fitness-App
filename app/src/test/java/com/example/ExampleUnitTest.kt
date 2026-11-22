package com.example

import com.example.data.ai.CoachUserContext
import com.example.data.ai.PlaceholderAiCoachRepository
import com.example.data.catalog.MuscleGroup
import com.example.data.catalog.WellnessCatalog
import com.example.data.catalog.WorkoutCatalog
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun all_muscle_groups_have_exercises_and_programs() {
        MuscleGroup.entries.forEach { group ->
            assertTrue("Missing exercises for $group", WorkoutCatalog.exercisesFor(group).isNotEmpty())
            assertTrue("Missing programs for $group", WorkoutCatalog.programsFor(group).isNotEmpty())
        }
    }

    @Test
    fun all_required_features_are_registered() {
        val titles = WellnessCatalog.allFeatures.map { it.title }
        listOf(
            "Workout Hub", "Chest", "Biceps", "Triceps", "Shoulder",
            "Back", "Legs", "Abs", "Full Body", "Diet Plan",
            "Nutrition", "Weight Management", "AI Fitness Coach",
            "Exercise Library", "Progress Tracker", "Water Tracker",
            "Sleep Tracker", "Learning Center", "Community"
        ).forEach { expected ->
            assertTrue("Missing feature $expected", expected in titles)
        }
    }

    @Test
    fun placeholder_ai_coach_returns_labeled_sample_response() = runBlocking {
        val repo = PlaceholderAiCoachRepository()
        assertFalse(repo.isLiveGeminiConnected)
        val response = repo.askCoach(
            "Build me a chest workout",
            CoachUserContext(
                userName = "Alex",
                fitnessGoal = "Lean Muscle",
                currentWeightKg = 74f,
                targetWeightKg = 72f,
                caloriesToday = 1200,
                calorieGoal = 2300,
                proteinTodayG = 90,
                proteinGoalG = 160,
                waterTodayMl = 1500,
                waterGoalMl = 3000,
                lastSleepHours = 7.5f,
                workoutsCompletedTotal = 3
            )
        )
        assertTrue(response.isSampleResponse)
        assertTrue(response.text.contains("Sample AI Coach Response"))
    }
}
