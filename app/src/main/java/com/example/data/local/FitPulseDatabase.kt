package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        WorkoutLogEntity::class,
        MealLogEntity::class,
        WeightLogEntity::class,
        WaterLogEntity::class,
        SleepLogEntity::class,
        CommunityPostEntity::class,
        BookmarkEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitPulseDatabase : RoomDatabase() {
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun weightLogDao(): WeightLogDao
    abstract fun waterLogDao(): WaterLogDao
    abstract fun sleepLogDao(): SleepLogDao
    abstract fun communityPostDao(): CommunityPostDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: FitPulseDatabase? = null

        fun getInstance(context: Context): FitPulseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitPulseDatabase::class.java,
                    "fitpulse_db"
                )
                    .addCallback(SeedCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SeedCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        val postDao = database.communityPostDao()
                        if (postDao.count() == 0) {
                            val now = System.currentTimeMillis()
                            postDao.insertAll(
                                listOf(
                                    CommunityPostEntity(
                                        authorName = "Maya Lin",
                                        authorHandle = "@mayalifts",
                                        category = "Milestone",
                                        title = "Hit 12-week consistency streak on Upper/Lower split!",
                                        body = "Finally hit 225 lb squat for 5 clean reps after prioritizing 8h sleep and 140g protein daily. Progressive overload + recovery works.",
                                        likesCount = 42,
                                        isLiked = false,
                                        commentsText = "Coach Alex: Huge milestone! Form discipline pays off.\nSamir K: Inspiring consistency!",
                                        timestamp = now - 3_600_000L * 2
                                    ),
                                    CommunityPostEntity(
                                        authorName = "Devon Brooks",
                                        authorHandle = "@devon_runs",
                                        category = "Nutrition",
                                        title = "My 5-minute high-protein post-workout bowl",
                                        body = "180g grilled chicken breast, 150g jasmine rice, roasted broccoli, avocado slices, and spicy Greek yogurt drizzle. 610 kcal | 52g protein.",
                                        likesCount = 29,
                                        isLiked = false,
                                        commentsText = "Priya R: Greek yogurt drizzle is a game changer.",
                                        timestamp = now - 3_600_000L * 6
                                    ),
                                    CommunityPostEntity(
                                        authorName = "Elena Vance",
                                        authorHandle = "@elena_mobility",
                                        category = "Workout",
                                        title = "Shoulder health warm-up before Chest & Overhead days",
                                        body = "Spend 5 minutes doing band pull-aparts, scapular wall slides, and external rotations before heavy bench or shoulder press. Zero shoulder pinching.",
                                        likesCount = 37,
                                        isLiked = false,
                                        commentsText = "Marcus T: Tried this today and bench felt way more stable!",
                                        timestamp = now - 3_600_000L * 14
                                    ),
                                    CommunityPostEntity(
                                        authorName = "Tariq N.",
                                        authorHandle = "@tariq_fit",
                                        category = "Question",
                                        title = "Best rep range for stubborn biceps & triceps?",
                                        body = "Should I stick to 8-12 reps on incline curls and overhead extensions or mix in heavier 6-8 rep sets?",
                                        likesCount = 15,
                                        isLiked = false,
                                        commentsText = "Jordan P: 8-12 for stretch-focused isolation and 6-10 on weighted dips/chin-ups worked best for me.",
                                        timestamp = now - 3_600_000L * 24
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
