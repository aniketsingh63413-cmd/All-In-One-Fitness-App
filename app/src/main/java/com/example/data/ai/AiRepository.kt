package com.example.data.ai

import kotlinx.coroutines.delay

data class CoachUserContext(
    val userName: String,
    val fitnessGoal: String,
    val currentWeightKg: Float,
    val targetWeightKg: Float,
    val caloriesToday: Int,
    val calorieGoal: Int,
    val proteinTodayG: Int,
    val proteinGoalG: Int,
    val waterTodayMl: Int,
    val waterGoalMl: Int,
    val lastSleepHours: Float?,
    val workoutsCompletedTotal: Int
)

data class AiCoachResponse(
    val text: String,
    val isSampleResponse: Boolean = true,
    val suggestedActionLabel: String? = null,
    val suggestedRoute: String? = null
)

interface AiRepository {
    val isLiveGeminiConnected: Boolean
    suspend fun askCoach(prompt: String, context: CoachUserContext): AiCoachResponse
}

/**
 * Sample implementation used when Firebase AI Logic is not enabled.
 * All AI coach requests route through [AiRepository] so live Gemini can be plugged in seamlessly
 * once Firebase is enabled in AI Studio.
 */
class PlaceholderAiCoachRepository : AiRepository {
    override val isLiveGeminiConnected: Boolean = false

    override suspend fun askCoach(prompt: String, context: CoachUserContext): AiCoachResponse {
        delay(450)
        val q = prompt.lowercase()
        val remainingCal = (context.calorieGoal - context.caloriesToday).coerceAtLeast(0)
        val remainingProtein = (context.proteinGoalG - context.proteinTodayG).coerceAtLeast(0)
        val remainingWater = (context.waterGoalMl - context.waterTodayMl).coerceAtLeast(0)

        return when {
            q.contains("chest") || q.contains("bench") || q.contains("push") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nFor ${context.userName}'s Chest & Push session (${context.fitnessGoal}):\n" +
                    "1. Incline Dumbbell Press: 4 sets x 8-10 reps (90s rest)\n" +
                    "2. Flat Barbell Bench Press: 4 sets x 6-8 reps (2m rest)\n" +
                    "3. Cable Chest Fly (mid-to-low): 3 sets x 12-15 reps\n" +
                    "4. Weighted or Bodyweight Dips: 3 sets x 10-12 reps\n\n" +
                    "Form cue: Retract and depress shoulder blades before unracking, and control a 2-second eccentric.",
                suggestedActionLabel = "Open Chest Workouts",
                suggestedRoute = "muscle_workout/CHEST"
            )
            q.contains("arm") || q.contains("bicep") || q.contains("tricep") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nArm Hypertrophy Focus:\n" +
                    "• Biceps (Stretch + Peak): Incline Dumbbell Curls (3x10-12) + Hammer Curls (3x12)\n" +
                    "• Triceps (Long Head + Lateral): Overhead Cable Extension (3x12) + Rope Pushdowns (3x12-15)\n\n" +
                    "Keep elbows pinned and avoid swinging your torso on the final reps.",
                suggestedActionLabel = "Open Biceps Workout",
                suggestedRoute = "muscle_workout/BICEPS"
            )
            q.contains("leg") || q.contains("squat") || q.contains("glute") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nLower Body Strength & Balance:\n" +
                    "1. Barbell Back Squat or Goblet Squat: 4 sets x 6-10 reps\n" +
                    "2. Romanian Deadlift (Hamstrings/Glutes): 3 sets x 8-10 reps\n" +
                    "3. Bulgarian Split Squats: 3 sets x 10/leg\n" +
                    "4. Standing Calf Raises: 4 sets x 15 reps\n\n" +
                    "Hydration reminder: You still have ${remainingWater}ml left toward your daily ${context.waterGoalMl}ml goal.",
                suggestedActionLabel = "Open Legs Workouts",
                suggestedRoute = "muscle_workout/LEGS"
            )
            q.contains("back") || q.contains("pull") || q.contains("shoulder") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nV-Taper Back & Shoulder Blueprint:\n" +
                    "• Vertical Pull: Pull-Ups or Lat Pulldowns (4x8-10)\n" +
                    "• Horizontal Row: Chest-Supported Row (3x10-12)\n" +
                    "• Shoulders: Seated Overhead Press (4x8) + Lateral Raises (4x15)\n\n" +
                    "Pull with your elbows rather than your hands to maximize lat engagement.",
                suggestedActionLabel = "Open Back Workouts",
                suggestedRoute = "muscle_workout/BACK"
            )
            q.contains("abs") || q.contains("core") || q.contains("full body") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nCore & Full-Body Conditioning:\n" +
                    "• Hanging Leg Raises: 3 sets x 12-15 reps\n" +
                    "• Cable Woodchoppers: 3 sets x 12/side\n" +
                    "• Hollow Body Hold or Plank: 3 x 45s\n\n" +
                    "Exhale fully at peak contraction to engage the deep transverse abdominis.",
                suggestedActionLabel = "Open Abs Workouts",
                suggestedRoute = "muscle_workout/ABS"
            )
            q.contains("diet") || q.contains("meal") || q.contains("protein") || q.contains("calorie") || q.contains("macro") || q.contains("food") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nDaily Nutrition Check-in for ${context.userName}:\n" +
                    "• Calories logged: ${context.caloriesToday} / ${context.calorieGoal} kcal ($remainingCal kcal remaining)\n" +
                    "• Protein logged: ${context.proteinTodayG}g / ${context.proteinGoalG}g (${remainingProtein}g remaining)\n\n" +
                    "Sample next meal idea: 170g grilled salmon or tofu, 150g quinoa, steamed greens & olive oil (~520 kcal, 40g protein).",
                suggestedActionLabel = "Open Nutrition Tracker",
                suggestedRoute = "nutrition"
            )
            q.contains("weight") || q.contains("fat") || q.contains("lose") || q.contains("bulk") || q.contains("bmi") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nWeight Management Roadmap:\n" +
                    "• Current: ${"%.1f".format(context.currentWeightKg)} kg -> Target: ${"%.1f".format(context.targetWeightKg)} kg\n" +
                    "• Aim for a sustainable 0.3-0.5 kg/week rate with ${context.proteinGoalG}g daily protein to preserve lean muscle.\n" +
                    "• Combine 3-4 resistance sessions/week with 8,000+ daily steps.",
                suggestedActionLabel = "Open Weight Management",
                suggestedRoute = "weight_management"
            )
            q.contains("sleep") || q.contains("water") || q.contains("recover") || q.contains("tired") -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nRecovery & Readiness Summary:\n" +
                    "• Hydration today: ${context.waterTodayMl} / ${context.waterGoalMl} ml\n" +
                    "• Latest sleep logged: ${context.lastSleepHours?.let { "%.1f hrs".format(it) } ?: "Not logged yet"}\n\n" +
                    "Tip: Aim for 7.5-9 hours of consistent sleep and finish your remaining ${remainingWater}ml water before late evening.",
                suggestedActionLabel = "Open Sleep Tracker",
                suggestedRoute = "sleep_tracker"
            )
            else -> AiCoachResponse(
                text = "[Sample AI Coach Response]\n\nHi ${context.userName}! Here is your personalized snapshot for \"${context.fitnessGoal}\":\n" +
                    "• Workouts completed: ${context.workoutsCompletedTotal}\n" +
                    "• Today's calories: ${context.caloriesToday} / ${context.calorieGoal} kcal\n" +
                    "• Today's protein: ${context.proteinTodayG} / ${context.proteinGoalG} g\n" +
                    "• Hydration: ${context.waterTodayMl} / ${context.waterGoalMl} ml\n\n" +
                    "Ask me about any muscle split (Chest, Biceps, Triceps, Shoulder, Back, Legs, Abs, Full Body), macros, weight loss, or sleep recovery!",
                suggestedActionLabel = "Explore Workout Hub",
                suggestedRoute = "workout"
            )
        }
    }
}
