package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ExerciseItem
import com.example.data.catalog.MuscleGroup
import com.example.data.catalog.WorkoutCatalog
import com.example.data.catalog.WorkoutProgram
import com.example.ui.components.ExerciseDetailSheet
import com.example.ui.components.FeatureCardTile
import com.example.ui.components.FitPulseTopBar
import com.example.ui.components.TagChip
import com.example.ui.components.muscleGroupIcon
import com.example.ui.viewmodel.FitPulseUiState

@Composable
fun WorkoutHubScreen(
    state: FitPulseUiState,
    onNavigate: (String) -> Unit,
    onLogCustomWorkout: (title: String, group: MuscleGroup, durationMin: Int, calories: Int, sets: Int) -> Unit,
    onDeleteWorkoutLog: (Long) -> Unit,
    showBackButton: Boolean = false,
    onBack: () -> Unit = {}
) {
    var showCustomDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Workout Hub",
                subtitle = "${state.workoutLogs.size} workouts logged • ${state.todayCaloriesBurned} kcal today",
                onBack = if (showBackButton) onBack else null,
                actions = {
                    TextButton(
                        onClick = { showCustomDialog = true },
                        modifier = Modifier.testTag("workout_log_custom_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Log")
                    }
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val columns = if (maxWidth >= 600.dp) 4 else 2

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1100.dp)
                    .align(Alignment.TopCenter),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Train by Muscle Group", style = MaterialTheme.typography.headlineSmall)
                        TextButton(onClick = { onNavigate("exercise_library") }) {
                            Text("Exercise Library")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    AdaptiveGridRows(columns = columns, items = MuscleGroup.entries) { group ->
                        FeatureCardTile(
                            title = group.displayName,
                            subtitle = group.tagline,
                            icon = muscleGroupIcon(group),
                            accent = group.accentColor,
                            tag = "workout_split_${group.name}",
                            badge = "Start",
                            onClick = { onNavigate("muscle_workout/${group.name}") }
                        )
                    }
                }

                item {
                    Text("Featured Guided Programs", style = MaterialTheme.typography.headlineSmall)
                }

                items(WorkoutCatalog.programs.take(6)) { program ->
                    ProgramCard(
                        program = program,
                        onStart = { onNavigate("active_workout/${program.id}") },
                        onOpenMuscle = { onNavigate("muscle_workout/${program.muscleGroup.name}") }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recent Workout History", style = MaterialTheme.typography.headlineSmall)
                        TextButton(onClick = { onNavigate("progress_tracker") }) {
                            Text("Analytics")
                        }
                    }
                }

                if (state.workoutLogs.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.FitnessCenter,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Text("No workouts logged yet", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Pick a muscle group or start a guided program above to track sets, volume, and calories.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                OutlinedButton(onClick = { showCustomDialog = true }) {
                                    Text("Log Your First Workout")
                                }
                            }
                        }
                    }
                } else {
                    items(state.workoutLogs.take(10), key = { it.id }) { log ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(log.muscleGroup.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    Text(log.title, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${log.durationMinutes} min • ${log.setsCompleted} sets • ${log.caloriesBurned} kcal",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { onDeleteWorkoutLog(log.id) }) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete workout log")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCustomDialog) {
        LogCustomWorkoutDialog(
            onDismiss = { showCustomDialog = false },
            onSave = { title, group, duration, cals, sets ->
                onLogCustomWorkout(title, group, duration, cals, sets)
                showCustomDialog = false
            }
        )
    }
}

@Composable
fun ProgramCard(
    program: WorkoutProgram,
    onStart: () -> Unit,
    onOpenMuscle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TagChip(program.muscleGroup.displayName, program.muscleGroup.accentColor)
                    TagChip(program.level, MaterialTheme.colorScheme.secondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Timer, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${program.durationMinutes}m • ${program.estimatedCalories} kcal", style = MaterialTheme.typography.labelSmall)
                }
            }
            Text(program.title, style = MaterialTheme.typography.titleLarge)
            Text(
                program.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_program_${program.id}")
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Start Guided Session")
                }
                OutlinedButton(onClick = onOpenMuscle) {
                    Text("View Split")
                }
            }
        }
    }
}

@Composable
private fun LogCustomWorkoutDialog(
    onDismiss: () -> Unit,
    onSave: (String, MuscleGroup, Int, Int, Int) -> Unit
) {
    var title by remember { mutableStateOf("Strength Training Session") }
    var selectedGroup by remember { mutableStateOf(MuscleGroup.FULL_BODY) }
    var duration by remember { mutableStateOf("40") }
    var calories by remember { mutableStateOf("280") }
    var sets by remember { mutableStateOf("14") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Workout") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Workout Name") },
                    singleLine = true
                )
                Text("Muscle Group", style = MaterialTheme.typography.labelMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(MuscleGroup.entries) { group ->
                        FilterChip(
                            selected = selectedGroup == group,
                            onClick = { selectedGroup = group },
                            label = { Text(group.displayName) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it.filter { c -> c.isDigit() } },
                        label = { Text("Min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it.filter { c -> c.isDigit() } },
                        label = { Text("kcal") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = sets,
                        onValueChange = { sets = it.filter { c -> c.isDigit() } },
                        label = { Text("Sets") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        title.ifBlank { "${selectedGroup.displayName} Workout" },
                        selectedGroup,
                        duration.toIntOrNull() ?: 30,
                        calories.toIntOrNull() ?: 200,
                        sets.toIntOrNull() ?: 10
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ExerciseLibraryScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onToggleBookmark: (String) -> Unit,
    onLogExerciseSet: (ExerciseItem) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedMuscle by remember { mutableStateOf<MuscleGroup?>(null) }
    var selectedEquipment by remember { mutableStateOf("All") }
    var bookmarkedOnly by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<ExerciseItem?>(null) }

    val equipments = listOf("All", "Barbell", "Dumbbells", "Cable", "Machine", "Bodyweight")

    val filtered = remember(query, selectedMuscle, selectedEquipment, bookmarkedOnly, state.bookmarkedExerciseIds) {
        WorkoutCatalog.exercises.filter { ex ->
            val matchMuscle = selectedMuscle == null || ex.muscleGroup == selectedMuscle
            val matchEquip = selectedEquipment == "All" || ex.equipment.equals(selectedEquipment, ignoreCase = true)
            val matchBookmark = !bookmarkedOnly || state.bookmarkedExerciseIds.contains(ex.id)
            val q = query.trim().lowercase()
            val matchQuery = q.isEmpty() || ex.name.lowercase().contains(q) ||
                ex.secondaryMuscles.lowercase().contains(q) ||
                ex.muscleGroup.displayName.lowercase().contains(q)
            matchMuscle && matchEquip && matchBookmark && matchQuery
        }
    }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Exercise Library",
                subtitle = "${filtered.size} exercises with form guides",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1000.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search exercise, muscle, or cue...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exercise_library_search")
                    )
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                selected = selectedMuscle == null && !bookmarkedOnly,
                                onClick = {
                                    selectedMuscle = null
                                    bookmarkedOnly = false
                                },
                                label = { Text("All Muscles") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = bookmarkedOnly,
                                onClick = { bookmarkedOnly = !bookmarkedOnly },
                                leadingIcon = { Icon(Icons.Filled.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                label = { Text("Saved") }
                            )
                        }
                        items(MuscleGroup.entries) { group ->
                            FilterChip(
                                selected = selectedMuscle == group,
                                onClick = { selectedMuscle = if (selectedMuscle == group) null else group },
                                label = { Text(group.displayName) }
                            )
                        }
                    }
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(equipments) { eq ->
                            FilterChip(
                                selected = selectedEquipment == eq,
                                onClick = { selectedEquipment = eq },
                                label = { Text(eq) }
                            )
                        }
                    }
                }

                items(filtered, key = { it.id }) { exercise ->
                    ExerciseRowCard(
                        exercise = exercise,
                        isBookmarked = state.bookmarkedExerciseIds.contains(exercise.id),
                        onClick = { selectedExercise = exercise }
                    )
                }
            }
        }
    }

    selectedExercise?.let { exercise ->
        ExerciseDetailSheet(
            exercise = exercise,
            isBookmarked = state.bookmarkedExerciseIds.contains(exercise.id),
            onToggleBookmark = { onToggleBookmark(exercise.id) },
            onLogExerciseSet = { onLogExerciseSet(exercise) },
            onDismiss = { selectedExercise = null }
        )
    }
}

@Composable
fun ExerciseRowCard(
    exercise: ExerciseItem,
    isBookmarked: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("exercise_card_${exercise.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(exercise.muscleGroup.accentColor.copy(alpha = 0.14f))
            ) {
                Icon(
                    imageVector = muscleGroupIcon(exercise.muscleGroup),
                    contentDescription = null,
                    tint = exercise.muscleGroup.accentColor
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = exercise.muscleGroup.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = exercise.muscleGroup.accentColor,
                        fontWeight = FontWeight.Bold
                    )
                    if (isBookmarked) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Bookmarked",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${exercise.equipment} • ${exercise.defaultSets}x${exercise.defaultReps} • ${exercise.difficulty}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "View details", modifier = Modifier.size(18.dp))
        }
    }
}
