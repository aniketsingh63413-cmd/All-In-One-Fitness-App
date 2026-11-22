package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val muscleGroup: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val setsCompleted: Int,
    val volumeKg: Int,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "meal_logs")
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatsG: Int,
    val servingInfo: String = "1 serving",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Float,
    val bodyFatPercent: Float? = null,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "sleep_logs")
data class SleepLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hoursSlept: Float,
    val qualityRating: Int, // 1..5
    val bedtime: String,
    val wakeTime: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "community_posts")
data class CommunityPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorHandle: String,
    val category: String, // Milestone, Workout, Nutrition, Question
    val title: String,
    val body: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val commentsText: String = "", // Newline-separated "Author: comment"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks", primaryKeys = ["itemId", "itemType"])
data class BookmarkEntity(
    val itemId: String,
    val itemType: String, // "EXERCISE" or "ARTICLE"
    val timestamp: Long = System.currentTimeMillis()
)
