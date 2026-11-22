package com.example.data.catalog

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AccentAbs
import com.example.ui.theme.AccentBack
import com.example.ui.theme.AccentBiceps
import com.example.ui.theme.AccentChest
import com.example.ui.theme.AccentFullBody
import com.example.ui.theme.AccentLegs
import com.example.ui.theme.AccentShoulder
import com.example.ui.theme.AccentTriceps

enum class MuscleGroup(
    val displayName: String,
    val tagline: String,
    val focusMuscles: String,
    val accentColor: Color
) {
    CHEST("Chest", "Power & Upper Push", "Pectoralis Major, Clavicular Head, Anterior Delts", AccentChest),
    BICEPS("Biceps", "Arm Peak & Pull", "Biceps Brachii (Long & Short Head), Brachialis", AccentBiceps),
    TRICEPS("Triceps", "Lockout & Arm Mass", "Lateral, Long & Medial Triceps Heads", AccentTriceps),
    SHOULDER("Shoulder", "3D Deltoid Caps", "Anterior, Lateral & Rear Deltoids, Traps", AccentShoulder),
    BACK("Back", "Width & Thickness", "Latissimus Dorsi, Rhomboids, Erector Spinae", AccentBack),
    LEGS("Legs", "Lower Powerhouse", "Quadriceps, Hamstrings, Glutes, Calves", AccentLegs),
    ABS("Abs", "Core Stability", "Rectus Abdominis, Obliques, Transverse Core", AccentAbs),
    FULL_BODY("Full Body", "Total Conditioning", "Compound Multi-Joint Strength & Conditioning", AccentFullBody)
}

data class ExerciseItem(
    val id: String,
    val name: String,
    val muscleGroup: MuscleGroup,
    val secondaryMuscles: String,
    val equipment: String,
    val difficulty: String,
    val defaultSets: Int,
    val defaultReps: String,
    val restSeconds: Int,
    val caloriesPerSet: Int,
    val instructions: List<String>,
    val proTips: List<String>
)

data class WorkoutProgram(
    val id: String,
    val title: String,
    val muscleGroup: MuscleGroup,
    val level: String,
    val durationMinutes: Int,
    val estimatedCalories: Int,
    val description: String,
    val exerciseIds: List<String>
)

object WorkoutCatalog {

    val exercises: List<ExerciseItem> = listOf(
        // CHEST (6)
        ExerciseItem(
            id = "ex_chest_bench",
            name = "Barbell Bench Press",
            muscleGroup = MuscleGroup.CHEST,
            secondaryMuscles = "Anterior Delts, Triceps",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "6-8",
            restSeconds = 120,
            caloriesPerSet = 18,
            instructions = listOf(
                "Lie flat on the bench with eyes directly beneath the bar and feet planted firmly.",
                "Grip slightly wider than shoulder width and retract your shoulder blades into the bench.",
                "Lower the bar under control to mid-chest, keeping elbows tucked around 45 degrees.",
                "Drive feet into the floor and press the bar back up until arms are locked."
            ),
            proTips = listOf("Keep wrists stacked over elbows", "Pause 0.5s on chest to eliminate bounce")
        ),
        ExerciseItem(
            id = "ex_chest_incline_db",
            name = "Incline Dumbbell Press",
            muscleGroup = MuscleGroup.CHEST,
            secondaryMuscles = "Upper Chest, Front Delts",
            equipment = "Dumbbells",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "8-12",
            restSeconds = 90,
            caloriesPerSet = 15,
            instructions = listOf(
                "Set bench to a 30-degree incline and kick dumbbells up to shoulder height.",
                "Lower dumbbells slowly outside your upper chest until you feel a deep stretch.",
                "Press smoothly upward in a slight arc without clanging dumbbells at the top."
            ),
            proTips = listOf("30 degrees targets upper pecs without overworking front delts")
        ),
        ExerciseItem(
            id = "ex_chest_cable_fly",
            name = "Standing Cable Chest Fly",
            muscleGroup = MuscleGroup.CHEST,
            secondaryMuscles = "Serratus Anterior",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 11,
            instructions = listOf(
                "Set pulleys at shoulder height and step forward into a staggered stance.",
                "With a soft bend in the elbows, sweep hands together in front of your sternum.",
                "Squeeze pecs for 1 second, then open arms slowly under full tension."
            ),
            proTips = listOf("Think about hugging a wide tree rather than pressing")
        ),
        ExerciseItem(
            id = "ex_chest_dips",
            name = "Chest-Leaning Parallel Dips",
            muscleGroup = MuscleGroup.CHEST,
            secondaryMuscles = "Triceps, Anterior Delts",
            equipment = "Bodyweight",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "8-12",
            restSeconds = 90,
            caloriesPerSet = 14,
            instructions = listOf(
                "Grip parallel bars and lean torso forward 20-30 degrees with knees bent behind you.",
                "Lower until upper arms are parallel to the floor.",
                "Press back up while maintaining the forward chest lean."
            ),
            proTips = listOf("Flare elbows slightly more than tricep dips to shift load to lower pecs")
        ),
        ExerciseItem(
            id = "ex_chest_pushup",
            name = "Tempo Push-Ups",
            muscleGroup = MuscleGroup.CHEST,
            secondaryMuscles = "Triceps, Core",
            equipment = "Bodyweight",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-20",
            restSeconds = 60,
            caloriesPerSet = 12,
            instructions = listOf(
                "Start in a rigid high plank with hands slightly outside shoulder width.",
                "Lower your chest for a full 3-second count until just above the floor.",
                "Explode back up while keeping glutes and abs braced."
            ),
            proTips = listOf("Avoid letting hips sag on later reps")
        ),
        ExerciseItem(
            id = "ex_chest_pec_deck",
            name = "Machine Pec Deck Fly",
            muscleGroup = MuscleGroup.CHEST,
            secondaryMuscles = "Inner Pectorals",
            equipment = "Machine",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 10,
            instructions = listOf(
                "Adjust seat so handles align with mid-chest and keep shoulder blades pinned back.",
                "Bring arms together smoothly until handles nearly touch.",
                "Control the return until your chest is stretched without shoulders rolling forward."
            ),
            proTips = listOf("Great finisher for safe mechanical tension close to failure")
        ),

        // BICEPS (6)
        ExerciseItem(
            id = "ex_biceps_ez_curl",
            name = "EZ-Bar Standing Curl",
            muscleGroup = MuscleGroup.BICEPS,
            secondaryMuscles = "Brachioradialis, Forearms",
            equipment = "Barbell",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "8-12",
            restSeconds = 75,
            caloriesPerSet = 10,
            instructions = listOf(
                "Hold the EZ-bar on the angled grip with elbows pinned close to your ribs.",
                "Curl the bar upward by flexing purely at the elbow without swinging hips.",
                "Lower over a controlled 2-3 seconds until arms reach full extension."
            ),
            proTips = listOf("Angled grip keeps wrists comfortable under heavier loads")
        ),
        ExerciseItem(
            id = "ex_biceps_incline_curl",
            name = "Incline Dumbbell Curl",
            muscleGroup = MuscleGroup.BICEPS,
            secondaryMuscles = "Long Head Biceps",
            equipment = "Dumbbells",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "10-12",
            restSeconds = 75,
            caloriesPerSet = 9,
            instructions = listOf(
                "Sit on a 45-degree incline bench letting arms hang straight down behind your torso.",
                "Supinate palms as you curl dumbbells up while keeping upper arms perpendicular to floor.",
                "Lower completely to feel the deep long-head bicep stretch."
            ),
            proTips = listOf("Do not let elbows drift forward during the curl")
        ),
        ExerciseItem(
            id = "ex_biceps_hammer",
            name = "Cross-Body Hammer Curl",
            muscleGroup = MuscleGroup.BICEPS,
            secondaryMuscles = "Brachialis, Forearms",
            equipment = "Dumbbells",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "10-14",
            restSeconds = 60,
            caloriesPerSet = 9,
            instructions = listOf(
                "Hold dumbbells with neutral thumbs-up palms.",
                "Alternate curling each dumbbell across toward the opposite shoulder.",
                "Pause briefly at the top and lower smoothly."
            ),
            proTips = listOf("Builds arm thickness by emphasizing the brachialis underneath the biceps")
        ),
        ExerciseItem(
            id = "ex_biceps_preacher",
            name = "Preacher Bench Curl",
            muscleGroup = MuscleGroup.BICEPS,
            secondaryMuscles = "Short Head Biceps",
            equipment = "Machine",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "10-12",
            restSeconds = 60,
            caloriesPerSet = 9,
            instructions = listOf(
                "Rest upper arms flat against the angled pad with armpits tucked over the top edge.",
                "Curl upward smoothly without lifting elbows off the pad.",
                "Stop just shy of hyperextension at the bottom."
            ),
            proTips = listOf("Zero momentum allowed—strict bicep isolation")
        ),
        ExerciseItem(
            id = "ex_biceps_cable_bayesian",
            name = "Bayesian Cable Curl",
            muscleGroup = MuscleGroup.BICEPS,
            secondaryMuscles = "Long Head Biceps",
            equipment = "Cable",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 8,
            instructions = listOf(
                "Set single-arm pulley at lowest notch and step forward facing away from the stack.",
                "Keep upper arm slightly behind your torso to pre-stretch the bicep.",
                "Curl handle forward and up while keeping shoulder locked."
            ),
            proTips = listOf("Constant cable resistance makes the stretched bottom range extra effective")
        ),
        ExerciseItem(
            id = "ex_biceps_chinup",
            name = "Underhand Close Chin-Up",
            muscleGroup = MuscleGroup.BICEPS,
            secondaryMuscles = "Latissimus Dorsi, Core",
            equipment = "Bodyweight",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "6-10",
            restSeconds = 90,
            caloriesPerSet = 14,
            instructions = listOf(
                "Grip the bar with palms facing you at shoulder width.",
                "Pull chest toward the bar by driving elbows down and squeezing biceps.",
                "Lower all the way down under control."
            ),
            proTips = listOf("One of the highest-loading bodyweight bicep builders")
        ),

        // TRICEPS (6)
        ExerciseItem(
            id = "ex_triceps_rope_pushdown",
            name = "Cable Rope Pushdown",
            muscleGroup = MuscleGroup.TRICEPS,
            secondaryMuscles = "Lateral & Medial Heads",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 9,
            instructions = listOf(
                "Attach rope to high pulley and tuck elbows tightly against your sides.",
                "Press hands downward and split the rope ends apart at full lockout.",
                "Raise forearms back up to chest height without letting elbows flare forward."
            ),
            proTips = listOf("Slight hip hinge gives the cable a clean vertical path")
        ),
        ExerciseItem(
            id = "ex_triceps_overhead_ext",
            name = "Overhead Cable Rope Extension",
            muscleGroup = MuscleGroup.TRICEPS,
            secondaryMuscles = "Long Head Triceps",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "10-14",
            restSeconds = 60,
            caloriesPerSet = 10,
            instructions = listOf(
                "Face away from a waist-high pulley holding the rope behind your head.",
                "Lean forward slightly and keep elbows pointed up and forward.",
                "Extend arms overhead to full contraction, then bend deeply behind head."
            ),
            proTips = listOf("Overhead arm position maximally stretches the tricep long head")
        ),
        ExerciseItem(
            id = "ex_triceps_skullcrusher",
            name = "EZ-Bar Skull Crushers",
            muscleGroup = MuscleGroup.TRICEPS,
            secondaryMuscles = "Anconeus",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "8-12",
            restSeconds = 75,
            caloriesPerSet = 11,
            instructions = listOf(
                "Lie on a flat bench holding an EZ-bar with arms angled slightly back behind vertical.",
                "Bend only at the elbows to lower the bar just behind your forehead.",
                "Extend strongly back to lockout while maintaining upper-arm angle."
            ),
            proTips = listOf("Angling arms slightly back keeps tension on triceps even at the top")
        ),
        ExerciseItem(
            id = "ex_triceps_close_bench",
            name = "Close-Grip Barbell Press",
            muscleGroup = MuscleGroup.TRICEPS,
            secondaryMuscles = "Chest, Front Delts",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "6-10",
            restSeconds = 90,
            caloriesPerSet = 15,
            instructions = listOf(
                "Grip barbell at shoulder width (not overly narrow to protect wrists).",
                "Keep elbows tucked close to torso as you lower bar to lower chest.",
                "Drive bar up powerfully through tricep lockout."
            ),
            proTips = listOf("Best compound mass builder for all three tricep heads")
        ),
        ExerciseItem(
            id = "ex_triceps_bench_dip",
            name = "Bench / Upright Tricep Dips",
            muscleGroup = MuscleGroup.TRICEPS,
            secondaryMuscles = "Anterior Delts",
            equipment = "Bodyweight",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-16",
            restSeconds = 60,
            caloriesPerSet = 11,
            instructions = listOf(
                "Place hands on bench edge behind you with torso upright.",
                "Lower hips straight down until elbows hit 90 degrees.",
                "Press through palms to full elbow extension."
            ),
            proTips = listOf("Keep torso vertical to keep emphasis on triceps instead of chest")
        ),
        ExerciseItem(
            id = "ex_triceps_db_kickback",
            name = "Single-Arm Cable Kickback",
            muscleGroup = MuscleGroup.TRICEPS,
            secondaryMuscles = "Lateral Triceps Head",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 45,
            caloriesPerSet = 8,
            instructions = listOf(
                "Hinge torso forward holding a cable handle (no attachment or single handle).",
                "Pin elbow high beside your ribs and extend forearm straight back.",
                "Hold peak squeeze for 1 second before returning to 90 degrees."
            ),
            proTips = listOf("Cables provide peak resistance at full lockout unlike dumbbells")
        ),

        // SHOULDER (6)
        ExerciseItem(
            id = "ex_shoulder_ohp",
            name = "Seated Dumbbell Shoulder Press",
            muscleGroup = MuscleGroup.SHOULDER,
            secondaryMuscles = "Triceps, Upper Chest",
            equipment = "Dumbbells",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "8-10",
            restSeconds = 90,
            caloriesPerSet = 14,
            instructions = listOf(
                "Set bench upright (80 degrees) and hold dumbbells at ear level with elbows slightly forward.",
                "Press dumbbells overhead in a smooth arc without overarching lower back.",
                "Lower slowly until dumbbells graze shoulder level."
            ),
            proTips = listOf("Slightly tucking elbows in front of torso protects the shoulder joint")
        ),
        ExerciseItem(
            id = "ex_shoulder_lat_raise",
            name = "Dumbbell Lateral Raise",
            muscleGroup = MuscleGroup.SHOULDER,
            secondaryMuscles = "Lateral Deltoids, Upper Traps",
            equipment = "Dumbbells",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "12-18",
            restSeconds = 60,
            caloriesPerSet = 9,
            instructions = listOf(
                "Stand tall with slight forward torso lean and dumbbells at your sides.",
                "Sweep arms outward in the scapular plane (30 degrees forward) to shoulder height.",
                "Lower slowly over 2 seconds without dropping arms to rest."
            ),
            proTips = listOf("Lead with your elbows and reach outward toward the walls")
        ),
        ExerciseItem(
            id = "ex_shoulder_face_pull",
            name = "Cable Rope Face Pull",
            muscleGroup = MuscleGroup.SHOULDER,
            secondaryMuscles = "Rear Delts, Rotator Cuff, Mid Traps",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "15-20",
            restSeconds = 60,
            caloriesPerSet = 9,
            instructions = listOf(
                "Set rope pulley at upper-chest height and grasp with thumbs pointing backward.",
                "Pull rope toward your forehead while externally rotating hands apart.",
                "Pause in a double-biceps pose before controlling the return."
            ),
            proTips = listOf("Essential for shoulder posture and rear-delt balance")
        ),
        ExerciseItem(
            id = "ex_shoulder_barbell_ohp",
            name = "Standing Barbell Overhead Press",
            muscleGroup = MuscleGroup.SHOULDER,
            secondaryMuscles = "Core, Triceps, Traps",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "5-8",
            restSeconds = 120,
            caloriesPerSet = 16,
            instructions = listOf(
                "Rest bar on upper chest/front delts with glutes and abs squeezed tight.",
                "Press bar straight up past your face, pushing your head slightly through at the top.",
                "Lower under control back to collarbones."
            ),
            proTips = listOf("Squeeze glutes hard to prevent lower-back hyperextension")
        ),
        ExerciseItem(
            id = "ex_shoulder_reverse_pec",
            name = "Reverse Pec Deck Rear Delt Fly",
            muscleGroup = MuscleGroup.SHOULDER,
            secondaryMuscles = "Rhomboids, Rear Delts",
            equipment = "Machine",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-16",
            restSeconds = 60,
            caloriesPerSet = 9,
            instructions = listOf(
                "Face the machine pad and hold handles with palms facing inward or down.",
                "Sweep arms outward and back using rear shoulders rather than pinching traps.",
                "Control the eccentric back to the start."
            ),
            proTips = listOf("Think about pushing handles away from you in a wide arc")
        ),
        ExerciseItem(
            id = "ex_shoulder_arnold",
            name = "Arnold Dumbbell Press",
            muscleGroup = MuscleGroup.SHOULDER,
            secondaryMuscles = "Front & Lateral Delts, Triceps",
            equipment = "Dumbbells",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "10-12",
            restSeconds = 75,
            caloriesPerSet = 12,
            instructions = listOf(
                "Start with dumbbells in front of chest, palms facing you.",
                "Rotate palms outward as you press overhead to full extension.",
                "Reverse the rotation smoothly on the way down."
            ),
            proTips = listOf("Smooth rotation hits both anterior and lateral deltoid fibers")
        ),

        // BACK (6)
        ExerciseItem(
            id = "ex_back_pullup",
            name = "Wide-Grip Pull-Ups",
            muscleGroup = MuscleGroup.BACK,
            secondaryMuscles = "Biceps, Rear Delts, Core",
            equipment = "Bodyweight",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "6-10",
            restSeconds = 90,
            caloriesPerSet = 15,
            instructions = listOf(
                "Hang from bar slightly wider than shoulder width and engage scapulae downward.",
                "Drive elbows down toward your hips to bring upper chest toward the bar.",
                "Lower smoothly to a full dead hang."
            ),
            proTips = listOf("Avoid craning your neck upward to reach the bar")
        ),
        ExerciseItem(
            id = "ex_back_lat_pulldown",
            name = "Neutral-Grip Lat Pulldown",
            muscleGroup = MuscleGroup.BACK,
            secondaryMuscles = "Biceps, Mid Back",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "10-12",
            restSeconds = 75,
            caloriesPerSet = 12,
            instructions = listOf(
                "Lock thighs under pad and lean torso back slightly (10-15 degrees).",
                "Pull handle down toward upper sternum by driving elbows straight down.",
                "Control the bar all the way up until lats feel a full stretch."
            ),
            proTips = listOf("Use straps if grip fatigues before your lats do")
        ),
        ExerciseItem(
            id = "ex_back_barbell_row",
            name = "Bent-Over Barbell Row",
            muscleGroup = MuscleGroup.BACK,
            secondaryMuscles = "Lats, Rhomboids, Erectors",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "8-10",
            restSeconds = 90,
            caloriesPerSet = 16,
            instructions = listOf(
                "Hinge at the hips to roughly 45 degrees with a flat braced spine.",
                "Row the bar smoothly into your lower ribcage / navel.",
                "Lower under control without jerking your torso upright."
            ),
            proTips = listOf("Keep chest proud and pull elbows back behind your torso")
        ),
        ExerciseItem(
            id = "ex_back_seated_row",
            name = "Seated Cable Row",
            muscleGroup = MuscleGroup.BACK,
            secondaryMuscles = "Rhomboids, Mid Traps, Biceps",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "10-12",
            restSeconds = 75,
            caloriesPerSet = 12,
            instructions = listOf(
                "Sit tall with knees slightly bent and brace your core.",
                "Pull V-bar toward your stomach while squeezing shoulder blades together.",
                "Allow shoulders to protract slightly forward at full extension for stretch."
            ),
            proTips = listOf("Pause for 1 second with shoulder blades pinched")
        ),
        ExerciseItem(
            id = "ex_back_single_db_row",
            name = "Single-Arm Dumbbell Row",
            muscleGroup = MuscleGroup.BACK,
            secondaryMuscles = "Lats, Rear Delts",
            equipment = "Dumbbells",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "10-12",
            restSeconds = 60,
            caloriesPerSet = 12,
            instructions = listOf(
                "Support one hand and knee on a bench with a flat back.",
                "Sweep dumbbell backward in an arc toward your hip pocket.",
                "Lower completely to stretch the lat at the bottom."
            ),
            proTips = listOf("Row toward your hip, not straight up to your armpit")
        ),
        ExerciseItem(
            id = "ex_back_hyperext",
            name = "45° Back Extension",
            muscleGroup = MuscleGroup.BACK,
            secondaryMuscles = "Erector Spinae, Glutes, Hamstrings",
            equipment = "Bodyweight",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 10,
            instructions = listOf(
                "Set hip pad just below hip bones so pelvis moves freely.",
                "Hinge down with a neutral spine, then squeeze glutes and lower back up to straight line.",
                "Avoid hyperextending past neutral at the top."
            ),
            proTips = listOf("Builds resilient lower-back endurance for heavy compound lifts")
        ),

        // LEGS (6)
        ExerciseItem(
            id = "ex_legs_squat",
            name = "Barbell Back Squat",
            muscleGroup = MuscleGroup.LEGS,
            secondaryMuscles = "Glutes, Core, Lower Back",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "6-8",
            restSeconds = 150,
            caloriesPerSet = 22,
            instructions = listOf(
                "Set bar comfortably across upper traps, brace core 360 degrees, and unlock knees/hips.",
                "Sit down between your heels until hip crease drops below knees.",
                "Drive smoothly out of the hole keeping chest up and knees tracking over toes."
            ),
            proTips = listOf("Take a deep diaphragmatic breath before each rep and brace hard")
        ),
        ExerciseItem(
            id = "ex_legs_rdl",
            name = "Romanian Deadlift (RDL)",
            muscleGroup = MuscleGroup.LEGS,
            secondaryMuscles = "Hamstrings, Glutes, Lower Back",
            equipment = "Barbell",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "8-10",
            restSeconds = 120,
            caloriesPerSet = 19,
            instructions = listOf(
                "Start standing with soft knee bend and bar touching upper thighs.",
                "Push hips straight backward keeping bar shaved closely along legs.",
                "Stop at mid-shin once hamstrings hit max stretch, then drive hips forward."
            ),
            proTips = listOf("Focus on pushing hips back, not bending lower back downward")
        ),
        ExerciseItem(
            id = "ex_legs_bulgarian",
            name = "Bulgarian Split Squat",
            muscleGroup = MuscleGroup.LEGS,
            secondaryMuscles = "Glutes, Core Stabilizers",
            equipment = "Dumbbells",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "10/leg",
            restSeconds = 90,
            caloriesPerSet = 16,
            instructions = listOf(
                "Place rear laces on a bench behind you and step front foot comfortably forward.",
                "Lower rear knee straight down toward the floor with slight forward torso lean.",
                "Press hard through the full front foot to return to standing."
            ),
            proTips = listOf("Unilateral king for eliminating left/right leg strength imbalances")
        ),
        ExerciseItem(
            id = "ex_legs_leg_press",
            name = "45° Machine Leg Press",
            muscleGroup = MuscleGroup.LEGS,
            secondaryMuscles = "Quads, Glutes",
            equipment = "Machine",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "10-14",
            restSeconds = 90,
            caloriesPerSet = 16,
            instructions = listOf(
                "Plant feet shoulder-width on sled and keep lower back pressed firmly into seat pad.",
                "Lower sled as deep as possible without hips curling off the pad.",
                "Press smoothly without snapping knees locked at the top."
            ),
            proTips = listOf("Lower foot placement biases quads; higher placement biases glutes")
        ),
        ExerciseItem(
            id = "ex_legs_leg_curl",
            name = "Seated Hamstring Curl",
            muscleGroup = MuscleGroup.LEGS,
            secondaryMuscles = "Calves",
            equipment = "Machine",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 10,
            instructions = listOf(
                "Secure thigh pad snug and align knee joint with machine pivot.",
                "Pull heels powerfully beneath the seat and pause 1 second.",
                "Control return to full knee extension."
            ),
            proTips = listOf("Seated curls train hamstrings in a stretched hip-flexed position")
        ),
        ExerciseItem(
            id = "ex_legs_calf_raise",
            name = "Standing Calf Raises",
            muscleGroup = MuscleGroup.LEGS,
            secondaryMuscles = "Gastrocnemius, Soleus",
            equipment = "Machine",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "12-20",
            restSeconds = 45,
            caloriesPerSet = 8,
            instructions = listOf(
                "Place balls of feet on edge of step and lower heels slowly for a 2-second stretch.",
                "Drive high onto big toes and pause 1 second at top contraction.",
                "Avoid bouncing quickly at the bottom."
            ),
            proTips = listOf("Pausing at the bottom kills Achilles tendon bounce and grows calves faster")
        ),

        // ABS (6)
        ExerciseItem(
            id = "ex_abs_hanging_raise",
            name = "Hanging Leg Raises",
            muscleGroup = MuscleGroup.ABS,
            secondaryMuscles = "Hip Flexors, Forearms",
            equipment = "Bodyweight",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "10-15",
            restSeconds = 60,
            caloriesPerSet = 11,
            instructions = listOf(
                "Hang steadily from pull-up bar without swinging.",
                "Curl pelvis upward toward ribs as you raise legs (or bent knees).",
                "Lower slowly with zero momentum."
            ),
            proTips = listOf("Focus on curling your tailbone forward rather than just lifting feet")
        ),
        ExerciseItem(
            id = "ex_abs_cable_crunch",
            name = "Kneeling Cable Crunch",
            muscleGroup = MuscleGroup.ABS,
            secondaryMuscles = "Rectus Abdominis",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "12-16",
            restSeconds = 60,
            caloriesPerSet = 10,
            instructions = listOf(
                "Kneel beneath high pulley holding rope beside your temples.",
                "Lock hips stationary and curl spine downward bringing elbows toward thighs.",
                "Exhale hard at peak crunch and return slowly."
            ),
            proTips = listOf("Your hips shouldn't sit back onto heels—flex the spine only")
        ),
        ExerciseItem(
            id = "ex_abs_plank",
            name = "RKC Hardstyle Plank",
            muscleGroup = MuscleGroup.ABS,
            secondaryMuscles = "Transverse Abdominis, Glutes",
            equipment = "Bodyweight",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "40s",
            restSeconds = 45,
            caloriesPerSet = 8,
            instructions = listOf(
                "Set forearms on floor directly under shoulders.",
                "Squeeze glutes, quads, and pull elbows toward toes isometrically.",
                "Breathe shallowly behind a tight abdominal brace."
            ),
            proTips = listOf("Creating maximal full-body tension makes 30-45s far harder than 2m passive planks")
        ),
        ExerciseItem(
            id = "ex_abs_woodchop",
            name = "Cable Rotational Woodchopper",
            muscleGroup = MuscleGroup.ABS,
            secondaryMuscles = "Obliques, Transverse Core",
            equipment = "Cable",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12/side",
            restSeconds = 45,
            caloriesPerSet = 10,
            instructions = listOf(
                "Stand sideways to a chest-high pulley holding handle with both arms extended.",
                "Rotate torso explosively across your body while pivoting rear foot slightly.",
                "Control the return back toward the stack."
            ),
            proTips = listOf("Arms stay long—power comes from ribcage rotation")
        ),
        ExerciseItem(
            id = "ex_abs_bicycle",
            name = "Slow Bicycle Crunch",
            muscleGroup = MuscleGroup.ABS,
            secondaryMuscles = "Obliques, Lower Abs",
            equipment = "Bodyweight",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "20 total",
            restSeconds = 45,
            caloriesPerSet = 9,
            instructions = listOf(
                "Press lower back flat into the mat with fingertips lightly behind ears.",
                "Rotate shoulder toward opposite knee while extending other leg low.",
                "Hold each twist for a half-second beat."
            ),
            proTips = listOf("Slow tempo outperforms frantic fast reps every time")
        ),
        ExerciseItem(
            id = "ex_abs_deadbug",
            name = "Weighted Dead Bug",
            muscleGroup = MuscleGroup.ABS,
            secondaryMuscles = "Deep Core Stabilizers",
            equipment = "Bodyweight",
            difficulty = "Beginner",
            defaultSets = 3,
            defaultReps = "12/side",
            restSeconds = 45,
            caloriesPerSet = 8,
            instructions = listOf(
                "Lie on back with knees at 90 degrees and arms straight up.",
                "Extend opposite arm and leg slowly while keeping lower back glued to floor.",
                "Exhale as you return to center."
            ),
            proTips = listOf("If your lower back arches off the floor, reduce leg lowering depth")
        ),

        // FULL BODY (6)
        ExerciseItem(
            id = "ex_full_kb_swing",
            name = "Kettlebell / Dumbbell Swing",
            muscleGroup = MuscleGroup.FULL_BODY,
            secondaryMuscles = "Glutes, Hamstrings, Back, Shoulders",
            equipment = "Dumbbells",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "15-20",
            restSeconds = 60,
            caloriesPerSet = 18,
            instructions = listOf(
                "Hinge hips back with flat spine and snap hips forward explosively.",
                "Let momentum float weight to chest height—do not muscle it up with arms.",
                "Wait until weight approaches hips before hinging back."
            ),
            proTips = listOf("Builds posterior-chain power and sky-high conditioning simultaneously")
        ),
        ExerciseItem(
            id = "ex_full_thruster",
            name = "Dumbbell Squat to Press Thruster",
            muscleGroup = MuscleGroup.FULL_BODY,
            secondaryMuscles = "Quads, Glutes, Shoulders, Core",
            equipment = "Dumbbells",
            difficulty = "Intermediate",
            defaultSets = 4,
            defaultReps = "10-12",
            restSeconds = 75,
            caloriesPerSet = 20,
            instructions = listOf(
                "Hold dumbbells at shoulders and squat to parallel.",
                "Drive violently out of the squat and use leg momentum to press dumbbells overhead.",
                "Lower weights back to shoulders smoothly into next rep."
            ),
            proTips = listOf("Exhale sharply at the top of every press")
        ),
        ExerciseItem(
            id = "ex_full_burpee",
            name = "Athletic Chest-to-Floor Burpee",
            muscleGroup = MuscleGroup.FULL_BODY,
            secondaryMuscles = "Chest, Quads, Core, Cardio",
            equipment = "Bodyweight",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            caloriesPerSet = 19,
            instructions = listOf(
                "Squat hands to floor, hop feet back into plank, and touch chest down.",
                "Push up, snap feet flat outside hands, and jump straight up with a light clap.",
                "Maintain steady rhythmic breathing."
            ),
            proTips = listOf("Step back instead of jumping if scaling for low-impact conditioning")
        ),
        ExerciseItem(
            id = "ex_full_deadlift",
            name = "Conventional Barbell Deadlift",
            muscleGroup = MuscleGroup.FULL_BODY,
            secondaryMuscles = "Posterior Chain, Traps, Grip, Core",
            equipment = "Barbell",
            difficulty = "Advanced",
            defaultSets = 3,
            defaultReps = "5",
            restSeconds = 150,
            caloriesPerSet = 24,
            instructions = listOf(
                "Stand with bar over mid-foot, hinge down, and pull slack out of the bar.",
                "Drive the floor away with your legs while keeping lats locked tight.",
                "Stand tall squeezing glutes at lockout, then hinge bar down cleanly."
            ),
            proTips = listOf("Never round lower back to initiate the pull off the floor")
        ),
        ExerciseItem(
            id = "ex_full_renegade_row",
            name = "Push-Up Renegade Row",
            muscleGroup = MuscleGroup.FULL_BODY,
            secondaryMuscles = "Chest, Lats, Core, Anti-Rotation",
            equipment = "Dumbbells",
            difficulty = "Intermediate",
            defaultSets = 3,
            defaultReps = "10 total",
            restSeconds = 75,
            caloriesPerSet = 15,
            instructions = listOf(
                "Set wide foot stance in plank holding hexagonal dumbbells.",
                "Perform one push-up, then row left dumbbell to hip without twisting hips.",
                "Repeat push-up and row right dumbbell."
            ),
            proTips = listOf("Wider feet make anti-rotation much cleaner")
        ),
        ExerciseItem(
            id = "ex_full_farmer_walk",
            name = "Heavy Farmer's Carry",
            muscleGroup = MuscleGroup.FULL_BODY,
            secondaryMuscles = "Forearms, Traps, Core, Legs",
            equipment = "Dumbbells",
            difficulty = "Beginner",
            defaultSets = 4,
            defaultReps = "40m walk",
            restSeconds = 60,
            caloriesPerSet = 14,
            instructions = listOf(
                "Pick up heavy dumbbells with a neutral spine like a deadlift.",
                "Walk briskly with tall posture, packed shoulders, and braced abs.",
                "Set weights down safely with legs."
            ),
            proTips = listOf("Underrated total-body strength, grip, and posture builder")
        )
    )

    val programs: List<WorkoutProgram> = listOf(
        // CHEST
        WorkoutProgram(
            id = "prog_chest_hypertrophy",
            title = "Chest Hypertrophy Blueprint",
            muscleGroup = MuscleGroup.CHEST,
            level = "Intermediate",
            durationMinutes = 42,
            estimatedCalories = 290,
            description = "Balanced upper, mid, and lower pectoral growth combining compound presses and deep-stretch flyes.",
            exerciseIds = listOf("ex_chest_incline_db", "ex_chest_bench", "ex_chest_cable_fly", "ex_chest_dips")
        ),
        WorkoutProgram(
            id = "prog_chest_express",
            title = "Home & Gym Chest Sculpt",
            muscleGroup = MuscleGroup.CHEST,
            level = "Beginner",
            durationMinutes = 28,
            estimatedCalories = 210,
            description = "Joint-friendly chest workout focusing on clean control and full muscle squeeze.",
            exerciseIds = listOf("ex_chest_incline_db", "ex_chest_pushup", "ex_chest_pec_deck")
        ),
        // BICEPS
        WorkoutProgram(
            id = "prog_biceps_peak",
            title = "Bicep Peak & Thickness",
            muscleGroup = MuscleGroup.BICEPS,
            level = "Intermediate",
            durationMinutes = 32,
            estimatedCalories = 185,
            description = "Targets long head, short head, and brachialis through varied shoulder angles.",
            exerciseIds = listOf("ex_biceps_ez_curl", "ex_biceps_incline_curl", "ex_biceps_hammer", "ex_biceps_preacher")
        ),
        WorkoutProgram(
            id = "prog_biceps_arm_pump",
            title = "Strict Cable & Pull Biceps",
            muscleGroup = MuscleGroup.BICEPS,
            level = "Beginner",
            durationMinutes = 25,
            estimatedCalories = 155,
            description = "Zero-momentum bicep blast combining chin-ups and continuous-tension cables.",
            exerciseIds = listOf("ex_biceps_chinup", "ex_biceps_cable_bayesian", "ex_biceps_hammer")
        ),
        // TRICEPS
        WorkoutProgram(
            id = "prog_triceps_horseshoe",
            title = "3-Head Tricep Horseshoe",
            muscleGroup = MuscleGroup.TRICEPS,
            level = "Intermediate",
            durationMinutes = 34,
            estimatedCalories = 205,
            description = "Heavy compound lockout paired with overhead long-head stretch and rope pushdowns.",
            exerciseIds = listOf("ex_triceps_close_bench", "ex_triceps_overhead_ext", "ex_triceps_rope_pushdown", "ex_triceps_db_kickback")
        ),
        WorkoutProgram(
            id = "prog_triceps_starter",
            title = "Arm Lockout Starter",
            muscleGroup = MuscleGroup.TRICEPS,
            level = "Beginner",
            durationMinutes = 24,
            estimatedCalories = 160,
            description = "Clean elbow-friendly tricep session ideal after push workouts or solo arm day.",
            exerciseIds = listOf("ex_triceps_rope_pushdown", "ex_triceps_overhead_ext", "ex_triceps_bench_dip")
        ),
        // SHOULDER
        WorkoutProgram(
            id = "prog_shoulder_3d",
            title = "3D Capped Shoulders",
            muscleGroup = MuscleGroup.SHOULDER,
            level = "Intermediate",
            durationMinutes = 38,
            estimatedCalories = 240,
            description = "Hits anterior, lateral, and posterior deltoids with posture-saving face pulls.",
            exerciseIds = listOf("ex_shoulder_ohp", "ex_shoulder_lat_raise", "ex_shoulder_face_pull", "ex_shoulder_reverse_pec")
        ),
        WorkoutProgram(
            id = "prog_shoulder_strength",
            title = "Overhead Strength & Stability",
            muscleGroup = MuscleGroup.SHOULDER,
            level = "Advanced",
            durationMinutes = 40,
            estimatedCalories = 265,
            description = "Barbell overhead pressing strength complemented by Arnold presses and lateral volume.",
            exerciseIds = listOf("ex_shoulder_barbell_ohp", "ex_shoulder_arnold", "ex_shoulder_lat_raise", "ex_shoulder_face_pull")
        ),
        // BACK
        WorkoutProgram(
            id = "prog_back_vtaper",
            title = "V-Taper Width & Mid-Back",
            muscleGroup = MuscleGroup.BACK,
            level = "Intermediate",
            durationMinutes = 45,
            estimatedCalories = 310,
            description = "Vertical pulling width combined with heavy horizontal rows for complete back density.",
            exerciseIds = listOf("ex_back_pullup", "ex_back_barbell_row", "ex_back_lat_pulldown", "ex_back_seated_row")
        ),
        WorkoutProgram(
            id = "prog_back_posture",
            title = "Posture & Lat Builder",
            muscleGroup = MuscleGroup.BACK,
            level = "Beginner",
            durationMinutes = 32,
            estimatedCalories = 220,
            description = "Accessible cable and dumbbell back training with lower-back resilience.",
            exerciseIds = listOf("ex_back_lat_pulldown", "ex_back_single_db_row", "ex_back_seated_row", "ex_back_hyperext")
        ),
        // LEGS
        WorkoutProgram(
            id = "prog_legs_power",
            title = "Lower Body Strength & Size",
            muscleGroup = MuscleGroup.LEGS,
            level = "Intermediate",
            durationMinutes = 48,
            estimatedCalories = 365,
            description = "Squat + RDL foundational pairing followed by unilateral quads and calf work.",
            exerciseIds = listOf("ex_legs_squat", "ex_legs_rdl", "ex_legs_bulgarian", "ex_legs_calf_raise")
        ),
        WorkoutProgram(
            id = "prog_legs_sculpt",
            title = "Quad & Hamstring Machine Flow",
            muscleGroup = MuscleGroup.LEGS,
            level = "Beginner",
            durationMinutes = 35,
            estimatedCalories = 275,
            description = "Joint-friendly leg press and isolation movements for balanced lower-body development.",
            exerciseIds = listOf("ex_legs_leg_press", "ex_legs_leg_curl", "ex_legs_bulgarian", "ex_legs_calf_raise")
        ),
        // ABS
        WorkoutProgram(
            id = "prog_abs_shred",
            title = "Core Stability & 6-Pack Circuit",
            muscleGroup = MuscleGroup.ABS,
            level = "Intermediate",
            durationMinutes = 22,
            estimatedCalories = 145,
            description = "Trains spinal flexion, anti-extension, and rotational obliques.",
            exerciseIds = listOf("ex_abs_hanging_raise", "ex_abs_cable_crunch", "ex_abs_woodchop", "ex_abs_plank")
        ),
        WorkoutProgram(
            id = "prog_abs_foundation",
            title = "Everyday Deep Core Builder",
            muscleGroup = MuscleGroup.ABS,
            level = "Beginner",
            durationMinutes = 18,
            estimatedCalories = 115,
            description = "Mat-friendly core sequence to improve brace strength and lower-back health.",
            exerciseIds = listOf("ex_abs_deadbug", "ex_abs_bicycle", "ex_abs_plank")
        ),
        // FULL BODY
        WorkoutProgram(
            id = "prog_full_metcon",
            title = "Full Body Athletic MetCon",
            muscleGroup = MuscleGroup.FULL_BODY,
            level = "Intermediate",
            durationMinutes = 38,
            estimatedCalories = 390,
            description = "High-output total body conditioning blending swings, thrusters, burpees, and carries.",
            exerciseIds = listOf("ex_full_thruster", "ex_full_kb_swing", "ex_full_renegade_row", "ex_full_burpee", "ex_full_farmer_walk")
        ),
        WorkoutProgram(
            id = "prog_full_strength",
            title = "Total Body Strength Trio",
            muscleGroup = MuscleGroup.FULL_BODY,
            level = "Advanced",
            durationMinutes = 50,
            estimatedCalories = 410,
            description = "Heavy compound movement pattern covering push, pull, hinge, squat, and loaded carry.",
            exerciseIds = listOf("ex_legs_squat", "ex_chest_bench", "ex_full_deadlift", "ex_back_pullup", "ex_full_farmer_walk")
        )
    )

    fun exercisesFor(group: MuscleGroup): List<ExerciseItem> =
        exercises.filter { it.muscleGroup == group }

    fun programsFor(group: MuscleGroup): List<WorkoutProgram> =
        programs.filter { it.muscleGroup == group }

    fun findExercise(id: String): ExerciseItem? = exercises.find { it.id == id }

    fun findProgram(id: String): WorkoutProgram? = programs.find { it.id == id }
}
