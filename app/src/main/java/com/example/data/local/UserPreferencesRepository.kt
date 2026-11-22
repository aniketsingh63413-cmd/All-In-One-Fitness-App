package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "fitpulse_prefs")

data class UserProfile(
    val name: String = "Athlete",
    val age: Int = 27,
    val heightCm: Int = 175,
    val currentWeightKg: Float = 74.0f,
    val targetWeightKg: Float = 71.0f,
    val dailyCalorieGoal: Int = 2300,
    val dailyProteinGoalG: Int = 160,
    val dailyCarbsGoalG: Int = 240,
    val dailyFatsGoalG: Int = 70,
    val dailyWaterGoalMl: Int = 3000,
    val dailySleepGoalHours: Float = 8.0f,
    val fitnessGoal: String = "Lean Muscle & Strength",
    val activityLevel: String = "Moderate (4-5 workouts/wk)",
    val darkThemeOverride: Boolean? = null
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val NAME = stringPreferencesKey("name")
        val AGE = intPreferencesKey("age")
        val HEIGHT_CM = intPreferencesKey("height_cm")
        val CURRENT_WEIGHT_KG = floatPreferencesKey("current_weight_kg")
        val TARGET_WEIGHT_KG = floatPreferencesKey("target_weight_kg")
        val DAILY_CALORIES = intPreferencesKey("daily_calories")
        val DAILY_PROTEIN = intPreferencesKey("daily_protein")
        val DAILY_CARBS = intPreferencesKey("daily_carbs")
        val DAILY_FATS = intPreferencesKey("daily_fats")
        val DAILY_WATER_ML = intPreferencesKey("daily_water_ml")
        val DAILY_SLEEP_HOURS = floatPreferencesKey("daily_sleep_hours")
        val FITNESS_GOAL = stringPreferencesKey("fitness_goal")
        val ACTIVITY_LEVEL = stringPreferencesKey("activity_level")
        val DARK_THEME_SET = booleanPreferencesKey("dark_theme_set")
        val DARK_THEME_VALUE = booleanPreferencesKey("dark_theme_value")
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        val darkOverride = if (prefs[Keys.DARK_THEME_SET] == true) {
            prefs[Keys.DARK_THEME_VALUE] ?: false
        } else {
            null
        }
        UserProfile(
            name = prefs[Keys.NAME] ?: "Athlete",
            age = prefs[Keys.AGE] ?: 27,
            heightCm = prefs[Keys.HEIGHT_CM] ?: 175,
            currentWeightKg = prefs[Keys.CURRENT_WEIGHT_KG] ?: 74.0f,
            targetWeightKg = prefs[Keys.TARGET_WEIGHT_KG] ?: 71.0f,
            dailyCalorieGoal = prefs[Keys.DAILY_CALORIES] ?: 2300,
            dailyProteinGoalG = prefs[Keys.DAILY_PROTEIN] ?: 160,
            dailyCarbsGoalG = prefs[Keys.DAILY_CARBS] ?: 240,
            dailyFatsGoalG = prefs[Keys.DAILY_FATS] ?: 70,
            dailyWaterGoalMl = prefs[Keys.DAILY_WATER_ML] ?: 3000,
            dailySleepGoalHours = prefs[Keys.DAILY_SLEEP_HOURS] ?: 8.0f,
            fitnessGoal = prefs[Keys.FITNESS_GOAL] ?: "Lean Muscle & Strength",
            activityLevel = prefs[Keys.ACTIVITY_LEVEL] ?: "Moderate (4-5 workouts/wk)",
            darkThemeOverride = darkOverride
        )
    }

    suspend fun updateProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = profile.name
            prefs[Keys.AGE] = profile.age
            prefs[Keys.HEIGHT_CM] = profile.heightCm
            prefs[Keys.CURRENT_WEIGHT_KG] = profile.currentWeightKg
            prefs[Keys.TARGET_WEIGHT_KG] = profile.targetWeightKg
            prefs[Keys.DAILY_CALORIES] = profile.dailyCalorieGoal
            prefs[Keys.DAILY_PROTEIN] = profile.dailyProteinGoalG
            prefs[Keys.DAILY_CARBS] = profile.dailyCarbsGoalG
            prefs[Keys.DAILY_FATS] = profile.dailyFatsGoalG
            prefs[Keys.DAILY_WATER_ML] = profile.dailyWaterGoalMl
            prefs[Keys.DAILY_SLEEP_HOURS] = profile.dailySleepGoalHours
            prefs[Keys.FITNESS_GOAL] = profile.fitnessGoal
            prefs[Keys.ACTIVITY_LEVEL] = profile.activityLevel
            if (profile.darkThemeOverride != null) {
                prefs[Keys.DARK_THEME_SET] = true
                prefs[Keys.DARK_THEME_VALUE] = profile.darkThemeOverride
            } else {
                prefs[Keys.DARK_THEME_SET] = false
            }
        }
    }

    suspend fun updateCurrentWeight(weightKg: Float) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CURRENT_WEIGHT_KG] = weightKg
        }
    }

    suspend fun updateWaterGoal(goalMl: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DAILY_WATER_ML] = goalMl.coerceIn(1000, 8000)
        }
    }

    suspend fun updateSleepGoal(goalHours: Float) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DAILY_SLEEP_HOURS] = goalHours.coerceIn(5.0f, 12.0f)
        }
    }
}
