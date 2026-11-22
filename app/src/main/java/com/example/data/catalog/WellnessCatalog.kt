package com.example.data.catalog

data class FoodItem(
    val id: String,
    val name: String,
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val servingInfo: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatsG: Int,
    val tags: String
)

data class DietPlanMeal(
    val mealType: String,
    val title: String,
    val itemsSummary: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatsG: Int
)

data class DietPlanTemplate(
    val id: String,
    val name: String,
    val goalBadge: String,
    val targetCalories: Int,
    val macroSplit: String,
    val description: String,
    val meals: List<DietPlanMeal>
)

data class ArticleSection(
    val heading: String,
    val body: String
)

data class LearningArticle(
    val id: String,
    val title: String,
    val category: String,
    val readMinutes: Int,
    val summary: String,
    val keyTakeaways: List<String>,
    val sections: List<ArticleSection>
)

data class AppFeatureDescriptor(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val route: String
)

object WellnessCatalog {

    val foods: List<FoodItem> = listOf(
        // Breakfast
        FoodItem("food_oats_whey", "Protein Oatmeal & Berries", "Breakfast", "1 bowl (70g oats + 1 scoop whey)", 410, 34, 52, 7, "High Protein, Fiber"),
        FoodItem("food_eggs_avocado", "3 Scrambled Eggs & Avocado Toast", "Breakfast", "3 eggs + 1 sourdough + 1/2 avocado", 460, 24, 28, 28, "Healthy Fats, Whole Food"),
        FoodItem("food_greek_yogurt", "Greek Yogurt Parfait & Chia", "Breakfast", "220g 0% Greek yogurt + honey + chia", 265, 25, 24, 6, "Probiotic, Quick"),
        FoodItem("food_egg_white_wrap", "Spinach Egg White Breakfast Wrap", "Breakfast", "4 egg whites + whole wheat tortilla + feta", 295, 28, 26, 8, "Lean, Low Calorie"),
        FoodItem("food_protein_pancakes", "Banana Oat Protein Pancakes", "Breakfast", "3 pancakes", 380, 30, 48, 7, "Pre-Workout"),
        // Lunch
        FoodItem("food_chicken_rice", "Grilled Chicken & Jasmine Rice Bowl", "Lunch", "180g chicken + 160g rice + broccoli", 540, 48, 56, 11, "Classic Muscle Fuel"),
        FoodItem("food_salmon_quinoa", "Baked Salmon, Quinoa & Edamame", "Lunch", "170g salmon + 140g quinoa + greens", 590, 42, 42, 24, "Omega-3, Anti-Inflammatory"),
        FoodItem("food_turkey_wrap", "Herb Turkey & Hummus Power Wrap", "Lunch", "150g turkey breast + wrap + veggies", 420, 36, 40, 11, "Portable, Balanced"),
        FoodItem("food_tofu_stirfry", "Crispy Tofu & Sobanoodle Stir-Fry", "Lunch", "200g firm tofu + veggies + sesame", 470, 29, 54, 15, "Plant-Based"),
        FoodItem("food_tuna_salad", "Mediterranean Tuna Chickpea Salad", "Lunch", "1 can tuna + chickpeas + olive oil", 430, 38, 32, 15, "High Protein, High Fiber"),
        // Dinner
        FoodItem("food_steak_sweet_potato", "Lean Sirloin Steak & Sweet Potato", "Dinner", "180g sirloin + 200g sweet potato + asparagus", 560, 46, 44, 19, "Iron-Rich, Strength"),
        FoodItem("food_cod_veggies", "Lemon Herb Cod & Roasted Veggies", "Dinner", "200g cod + zucchini + baby potatoes", 370, 40, 34, 7, "Ultra-Lean Cut"),
        FoodItem("food_lentil_curry", "Red Lentil & Paneer Protein Curry", "Dinner", "320g bowl + 1/2 cup basmati", 510, 28, 60, 16, "Vegetarian"),
        FoodItem("food_chicken_pasta", "High-Protein Chicken Pesto Pasta", "Dinner", "150g chicken + chickpea pasta", 580, 49, 48, 18, "Post-Workout Refuel"),
        FoodItem("food_shrimp_tacos", "Grilled Shrimp Avocado Tacos (3)", "Dinner", "180g shrimp + corn tortillas + slaw", 440, 35, 42, 13, "Light Dinner"),
        // Snacks
        FoodItem("food_whey_shake", "Whey Isolate Shake (Water/Almond Milk)", "Snack", "1 scoop (32g)", 130, 26, 3, 1, "Fast Absorbing"),
        FoodItem("food_almonds_apple", "Honeycrisp Apple & Raw Almonds", "Snack", "1 medium apple + 25g almonds", 240, 6, 28, 13, "Clean Energy"),
        FoodItem("food_cottage_cheese", "Cottage Cheese & Pineapple Cup", "Snack", "170g low-fat cottage cheese", 190, 21, 14, 4, "Slow Casein Night Snack"),
        FoodItem("food_protein_bar", "Dark Chocolate Sea Salt Protein Bar", "Snack", "1 bar (60g)", 210, 20, 22, 7, "On-The-Go"),
        FoodItem("food_edamame", "Steamed Sea-Salt Edamame", "Snack", "150g pods", 180, 17, 13, 7, "Plant Protein"),
        FoodItem("food_banana_pb", "Banana & Natural Peanut Butter", "Snack", "1 banana + 1 tbsp PB", 205, 5, 30, 8, "Pre-Workout Carbs")
    )

    val dietPlans: List<DietPlanTemplate> = listOf(
        DietPlanTemplate(
            id = "plan_lean_muscle",
            name = "Lean Muscle Builder",
            goalBadge = "Hypertrophy • +250 Surplus",
            targetCalories = 2550,
            macroSplit = "185g P • 280g C • 72g F",
            description = "High-protein peri-workout nutrition designed to fuel heavy resistance sessions without excess fat gain.",
            meals = listOf(
                DietPlanMeal("Breakfast", "Protein Oatmeal & Berries + 2 Boiled Eggs", "70g oats, 1 scoop whey, blueberries, 2 eggs", 550, 46, 54, 17),
                DietPlanMeal("Lunch", "Grilled Chicken & Jasmine Rice Bowl", "180g chicken, 180g cooked rice, steamed broccoli", 580, 48, 62, 12),
                DietPlanMeal("Snack", "Banana, Whey Shake & 20g Almonds", "Pre/post training quick carbs & amino acids", 360, 31, 35, 11),
                DietPlanMeal("Dinner", "Lean Sirloin Steak & Sweet Potato", "180g sirloin, 220g roasted sweet potato, asparagus", 610, 46, 48, 22),
                DietPlanMeal("Snack", "Cottage Cheese & Pineapple Cup", "Slow-digesting casein before sleep", 220, 22, 18, 5)
            )
        ),
        DietPlanTemplate(
            id = "plan_fat_loss",
            name = "Shred & Fat Loss",
            goalBadge = "Fat Loss • -450 Deficit",
            targetCalories = 1850,
            macroSplit = "165g P • 160g C • 55g F",
            description = "High-volume, high-satiety whole foods to preserve muscle mass while steadily shedding body fat.",
            meals = listOf(
                DietPlanMeal("Breakfast", "Spinach Egg White Wrap & Black Coffee", "4 egg whites, 1 whole egg, spinach, high-fiber wrap", 330, 32, 26, 10),
                DietPlanMeal("Lunch", "Mediterranean Tuna Chickpea Salad", "150g tuna, mixed greens, cucumber, 1 tbsp olive oil", 430, 38, 32, 15),
                DietPlanMeal("Snack", "Greek Yogurt Parfait & Berries", "220g 0% Greek yogurt + raspberries + cinnamon", 240, 25, 22, 4),
                DietPlanMeal("Dinner", "Lemon Herb Cod & Roasted Veggies", "220g cod, zucchini, bell peppers, 120g baby potatoes", 420, 44, 38, 8),
                DietPlanMeal("Snack", "Whey Isolate + Steamed Edamame", "Evening hunger blocker", 310, 43, 16, 8)
            )
        ),
        DietPlanTemplate(
            id = "plan_balanced",
            name = "Balanced Athletic Performance",
            goalBadge = "Maintenance • Recomp",
            targetCalories = 2250,
            macroSplit = "160g P • 240g C • 70g F",
            description = "Everyday sustainable macro balance supporting strength training, cardio, and steady mental focus.",
            meals = listOf(
                DietPlanMeal("Breakfast", "3 Scrambled Eggs & Avocado Toast", "Sourdough, 1/2 avocado, cherry tomatoes", 460, 24, 28, 28),
                DietPlanMeal("Lunch", "Herb Turkey & Hummus Power Wrap", "150g turkey, roasted peppers, spinach", 420, 36, 40, 11),
                DietPlanMeal("Snack", "Apple, Almonds & Whey Shake", "Afternoon energy + protein boost", 370, 32, 31, 14),
                DietPlanMeal("Dinner", "Baked Salmon, Quinoa & Edamame", "Omega-3 rich recovery dinner", 590, 42, 42, 24),
                DietPlanMeal("Snack", "Dark Chocolate Protein Bar", "Evening sweet craving solution", 210, 20, 22, 7)
            )
        ),
        DietPlanTemplate(
            id = "plan_plant_power",
            name = "Plant-Powered High Protein",
            goalBadge = "Plant-Based • 140g+ Protein",
            targetCalories = 2150,
            macroSplit = "142g P • 255g C • 62g F",
            description = "Complete plant amino acid pairings using tofu, lentils, edamame, chickpeas, and plant protein.",
            meals = listOf(
                DietPlanMeal("Breakfast", "Soy Protein Berry Oatmeal", "Oats, soy milk, 1 scoop pea/rice blend, flaxseed", 440, 34, 54, 10),
                DietPlanMeal("Lunch", "Crispy Tofu & Sobanoodle Stir-Fry", "200g firm tofu, broccoli, sesame ginger glaze", 490, 31, 56, 15),
                DietPlanMeal("Snack", "Steamed Edamame & Roasted Chickpeas", "Crunchy savory plant protein", 310, 24, 30, 10),
                DietPlanMeal("Dinner", "Red Lentil Coconut Curry & Quinoa", "Hearty fiber & iron-rich recovery meal", 560, 30, 68, 16),
                DietPlanMeal("Snack", "Banana & Peanut Butter + Plant Shake", "Night recovery", 340, 25, 36, 10)
            )
        )
    )

    val articles: List<LearningArticle> = listOf(
        LearningArticle(
            id = "art_hypertrophy",
            title = "Progressive Overload: The Science of Muscle Growth",
            category = "Hypertrophy",
            readMinutes = 4,
            summary = "How mechanical tension, proximity to failure, and weekly set volume drive reliable muscle hypertrophy.",
            keyTakeaways = listOf(
                "Aim for 10-20 hard working sets per muscle group per week.",
                "Stay within 1-3 reps shy of muscular failure (RIR 1-3) on most sets.",
                "Track reps and weight weekly—add 1 rep or 2.5 kg when you hit the top of your rep range."
            ),
            sections = listOf(
                ArticleSection(
                    "Why Mechanical Tension Matters Most",
                    "Muscle fibers grow primarily when exposed to high mechanical tension during slow, controlled contractions—especially in the stretched position of a lift. Lifting with clean tempo beats swinging heavy weights."
                ),
                ArticleSection(
                    "Double Progression Made Simple",
                    "If a program prescribes 3 sets of 8-12 reps, choose a weight where you hit 8-9 reps cleanly. Stay at that weight until you reach 12 reps on all sets, then increase the load slightly."
                )
            )
        ),
        LearningArticle(
            id = "art_protein_fatloss",
            title = "Protein & Calorie Deficit: Retaining Muscle While Cutting",
            category = "Nutrition Science",
            readMinutes = 5,
            summary = "Why protein targets rise during fat loss and how a moderate 300-500 kcal deficit protects strength.",
            keyTakeaways = listOf(
                "Target 1.6g to 2.2g of protein per kilogram of body weight.",
                "Keep calorie deficits moderate (15-20% below TDEE) to prevent metabolic slowdown and fatigue.",
                "Distribute protein across 3-5 meals with 25-45g per meal."
            ),
            sections = listOf(
                ArticleSection(
                    "The Thermic Effect & Satiety Advantage",
                    "Protein requires 20-30% of its calories simply to digest and significantly increases fullness hormones like PYY and GLP-1, making calorie control far easier."
                ),
                ArticleSection(
                    "Keep Lifting Heavy",
                    "Switching to ultra-light high-rep weights when cutting signals your body that strength is no longer needed. Maintain heavy compound lifts while moderating total fatigue."
                )
            )
        ),
        LearningArticle(
            id = "art_sleep_recovery",
            title = "Sleep Architecture, Growth Hormone & Gym Recovery",
            category = "Sleep & Recovery",
            readMinutes = 4,
            summary = "How 7.5-9 hours of quality sleep accelerates muscle repair, testosterone/IGF-1 balance, and fat loss.",
            keyTakeaways = listOf(
                "Deep slow-wave sleep triggers peak growth hormone release for tissue repair.",
                "Less than 6 hours of sleep drops muscle protein synthesis and spikes cravings.",
                "Keep a consistent wake time within 45 minutes even on weekends."
            ),
            sections = listOf(
                ArticleSection(
                    "Evening Wind-Down Protocol",
                    "Dim bright overhead screens 45 minutes before bed, keep your bedroom cool (18-19°C), and cut caffeine 8-10 hours prior to sleep."
                ),
                ArticleSection(
                    "Recognizing Overreaching",
                    "Resting heart rate spikes, poor grip strength, and restless sleep are early signs you need a deload week or extra rest day."
                )
            )
        ),
        LearningArticle(
            id = "art_hydration",
            title = "Hydration, Electrolytes & Training Performance",
            category = "Hydration",
            readMinutes = 3,
            summary = "Even a 2% drop in hydration reduces strength output, cognitive focus, and endurance.",
            keyTakeaways = listOf(
                "Baseline goal: ~35ml per kg of body weight daily, plus 500ml per hour of intense training.",
                "Include sodium and potassium around heavy sweat sessions to prevent cramping.",
                "Start the morning with 300-500ml water before coffee."
            ),
            sections = listOf(
                ArticleSection(
                    "Why Water Drives Pump & Endurance",
                    "Muscle tissue is roughly 75% water. Proper hydration maintains blood plasma volume so oxygen and nutrients reach working muscles efficiently."
                )
            )
        ),
        LearningArticle(
            id = "art_warmup_mobility",
            title = "5-Minute Joint-Saving Warm-Up Before Lifting",
            category = "Mobility & Injury Prevention",
            readMinutes = 4,
            summary = "Replace passive stretching before workouts with dynamic ramp-up sets and scapular activation.",
            keyTakeaways = listOf(
                "Save long static holds for post-workout; use dynamic movement pre-lift.",
                "Do 2-3 light warm-up sets ramping up to your working weight on main compounds.",
                "Warm up rotator cuffs before Chest/Shoulder days and hip flexors before Leg days."
            ),
            sections = listOf(
                ArticleSection(
                    "The RAMP Framework",
                    "Raise body temperature (2 min brisk cardio), Activate stabilizers (band pull-aparts, glute bridges), Mobilize joints through full range, and Potentiate with light bar warm-ups."
                )
            )
        ),
        LearningArticle(
            id = "art_cardio_zone2",
            title = "Zone 2 Cardio vs. HIIT for Heart Health & Longevity",
            category = "Conditioning",
            readMinutes = 4,
            summary = "How combining low-intensity aerobic base work with weekly intervals builds an unstoppable engine.",
            keyTakeaways = listOf(
                "Zone 2 (conversational pace, ~65-75% max HR) builds mitochondrial density without taxing recovery.",
                "Limit true all-out HIIT to 1-2 sessions per week so legs recover for squats/deadlifts.",
                "Aim for 150+ minutes of moderate weekly aerobic activity."
            ),
            sections = listOf(
                ArticleSection(
                    "The 80/20 Endurance Rule",
                    "Elite athletes perform roughly 80% of conditioning at steady conversational effort and 20% at high intensity. Better aerobic recovery actually lets you rest less between lifting sets."
                )
            )
        )
    )

    val allFeatures: List<AppFeatureDescriptor> = listOf(
        AppFeatureDescriptor("feat_workout", "Workout Hub", "Guided programs, live rest timer & custom log", "Training", "workout"),
        AppFeatureDescriptor("feat_chest", "Chest", "Upper, mid & lower pec workouts & form cues", "Muscle Split", "muscle_workout/CHEST"),
        AppFeatureDescriptor("feat_biceps", "Biceps", "Peak, thickness & incline stretch routines", "Muscle Split", "muscle_workout/BICEPS"),
        AppFeatureDescriptor("feat_triceps", "Triceps", "Long-head stretch & heavy lockout splits", "Muscle Split", "muscle_workout/TRICEPS"),
        AppFeatureDescriptor("feat_shoulder", "Shoulder", "3D delts, overhead press & rear-delt posture", "Muscle Split", "muscle_workout/SHOULDER"),
        AppFeatureDescriptor("feat_back", "Back", "Pull-ups, lat width & mid-back thickness", "Muscle Split", "muscle_workout/BACK"),
        AppFeatureDescriptor("feat_legs", "Legs", "Squats, RDLs, quads, glutes & calves", "Muscle Split", "muscle_workout/LEGS"),
        AppFeatureDescriptor("feat_abs", "Abs", "Core stability, obliques & hanging raises", "Muscle Split", "muscle_workout/ABS"),
        AppFeatureDescriptor("feat_full_body", "Full Body", "Total-body strength & athletic conditioning", "Muscle Split", "muscle_workout/FULL_BODY"),
        AppFeatureDescriptor("feat_diet_plan", "Diet Plan", "Structured daily meal templates for every goal", "Nutrition", "diet_plan"),
        AppFeatureDescriptor("feat_nutrition", "Nutrition", "Calorie & macro tracker + searchable food log", "Nutrition", "nutrition"),
        AppFeatureDescriptor("feat_weight", "Weight Management", "BMI, TDEE calculator, target pace & trend chart", "Body & Goals", "weight_management"),
        AppFeatureDescriptor("feat_ai_coach", "AI Fitness Coach", "Interactive fitness, nutrition & recovery coach", "Coaching & AI", "ai_coach"),
        AppFeatureDescriptor("feat_library", "Exercise Library", "48+ exercises with equipment filters & form cues", "Knowledge", "exercise_library"),
        AppFeatureDescriptor("feat_progress", "Progress Tracker", "Workout volume, calories, muscle balance & PRs", "Analytics", "progress_tracker"),
        AppFeatureDescriptor("feat_water", "Water Tracker", "Interactive hydration wave, quick-add & goals", "Daily Habits", "water_tracker"),
        AppFeatureDescriptor("feat_sleep", "Sleep Tracker", "Duration, quality stars & weekly sleep chart", "Daily Habits", "sleep_tracker"),
        AppFeatureDescriptor("feat_learning", "Learning Center", "Science-backed guides on training & nutrition", "Knowledge", "learning_center"),
        AppFeatureDescriptor("feat_community", "Community", "Share milestones, like posts & discuss tips", "Social", "community"),
        AppFeatureDescriptor("feat_profile", "Profile & Goals", "Body stats, daily targets & theme settings", "Account", "profile")
    )
}
