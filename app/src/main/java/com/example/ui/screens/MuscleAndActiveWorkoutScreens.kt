package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.catalog.ExerciseItem
import com.example.data.catalog.MuscleGroup
import com.example.data.catalog.WorkoutCatalog
import com.example.ui.components.ExerciseDetailSheet
import com.example.ui.components.FitPulseTopBar
import com.example.ui.components.TagChip
import com.example.ui.components.muscleGroupIcon
import com.example.ui.viewmodel.ActiveWorkoutState
import com.example.ui.viewmodel.FitPulseUiState

@Composable
fun MuscleWorkoutScreen(
    muscleGroup: MuscleGroup,
    state: FitPulseUiState,
    onBack: () -> Unit,
    onStartProgram: (String) -> Unit,
    onQuickLogMuscleSession: () -> Unit,
    onToggleBookmark: (String) -> Unit,
    onLogExerciseSet: (ExerciseItem) -> Unit
) {
    val programs = remember(muscleGroup) { WorkoutCatalog.programsFor(muscleGroup) }
    val exercises = remember(muscleGroup) { WorkoutCatalog.exercisesFor(muscleGroup) }
    var selectedExercise by remember { mutableStateOf<ExerciseItem?>(null) }
    var loggedBanner by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "${muscleGroup.displayName} Workouts",
                subtitle = muscleGroup.tagline,
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dedicated Muscle Focus Hero Card
                item {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFF0E1A1E),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            muscleGroup.accentColor.copy(alpha = 0.28f),
                                            Color(0xFF0E1A1E)
                                        )
                                    )
                                )
                                .padding(18.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(muscleGroup.accentColor.copy(alpha = 0.2f))
                                    ) {
                                        Icon(
                                            imageVector = muscleGroupIcon(muscleGroup),
                                            contentDescription = muscleGroup.displayName,
                                            tint = muscleGroup.accentColor,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "${muscleGroup.displayName.uppercase()} TRAINING",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = muscleGroup.accentColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = muscleGroup.tagline,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = Color.White
                                        )
                                    }
                                }
                                Text(
                                    text = "Target Anatomy: ${muscleGroup.focusMuscles}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFD0DDD9)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    if (programs.isNotEmpty()) {
                                        Button(
                                            onClick = { onStartProgram(programs.first().id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = muscleGroup.accentColor,
                                                contentColor = Color.White
                                            ),
                                            modifier = Modifier.testTag("muscle_start_primary_${muscleGroup.name}")
                                        ) {
                                            Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Start Workout")
                                        }
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            onQuickLogMuscleSession()
                                            loggedBanner = true
                                        },
                                        modifier = Modifier.testTag("muscle_quick_log_${muscleGroup.name}")
                                    ) {
                                        Text("Quick Log Session", color = Color.White)
                                    }
                                }
                                if (loggedBanner) {
                                    Text(
                                        text = "Session logged to your Progress Tracker!",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF4ADE80)
                                    )
                                }
                            }
                        }
                    }
                }

                // Guided Routines for this muscle group
                item {
                    Text(
                        text = "Guided ${muscleGroup.displayName} Programs",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(programs, key = { it.id }) { program ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TagChip(program.level, muscleGroup.accentColor)
                                Text(
                                    text = "${program.durationMinutes} min • ~${program.estimatedCalories} kcal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(program.title, style = MaterialTheme.typography.titleLarge)
                            Text(
                                program.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = { onStartProgram(program.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("muscle_prog_start_${program.id}")
                            ) {
                                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Launch Guided Player (${program.exerciseIds.size} Exercises)")
                            }
                        }
                    }
                }

                // Individual Exercises in this Muscle Group
                item {
                    Text(
                        text = "${muscleGroup.displayName} Exercises (${exercises.size})",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(exercises, key = { it.id }) { exercise ->
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
fun ActiveWorkoutScreen(
    programId: String,
    activeState: ActiveWorkoutState,
    onStartProgram: (String) -> Unit,
    onToggleSet: (exerciseId: String, targetSets: Int, restSeconds: Int) -> Unit,
    onStartRest: (Int) -> Unit,
    onSkipRest: () -> Unit,
    onFinishWorkout: () -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(programId) {
        if (activeState.program?.id != programId) {
            onStartProgram(programId)
        }
    }

    val program = activeState.program
    val totalTargetSets = activeState.exercises.sumOf { it.defaultSets }.coerceAtLeast(1)
    val totalCompletedSets = activeState.completedSets.values.sum()
    val progress = totalCompletedSets.toFloat() / totalTargetSets.toFloat()

    val minutes = activeState.elapsedSeconds / 60
    val seconds = activeState.elapsedSeconds % 60
    val elapsedFormatted = "%02d:%02d".format(minutes, seconds)

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = program?.title ?: "Active Workout",
                subtitle = "Elapsed: $elapsedFormatted • $totalCompletedSets/$totalTargetSets sets",
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
                    .widthIn(max = 900.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
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
                                Text("Session Progress", style = MaterialTheme.typography.titleMedium)
                                Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                            }
                            LinearProgressIndicator(
                                progress = { progress.coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape)
                            )

                            // Live Rest Timer Card
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (activeState.isRestRunning) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Filled.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Column {
                                            Text(
                                                text = if (activeState.isRestRunning) "Rest Timer: ${activeState.restSecondsRemaining}s" else "Rest Timer Ready",
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Text(
                                                text = if (activeState.isRestRunning) "Recover for next set" else "Tap a set pill to auto-start rest",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    if (activeState.isRestRunning) {
                                        TextButton(onClick = onSkipRest) {
                                            Text("Skip")
                                        }
                                    } else {
                                        TextButton(onClick = { onStartRest(60) }) {
                                            Text("60s Rest")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                items(activeState.exercises, key = { it.id }) { exercise ->
                    val doneSets = activeState.completedSets[exercise.id] ?: 0
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(exercise.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${exercise.defaultSets} sets x ${exercise.defaultReps} • ${exercise.equipment}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "$doneSets / ${exercise.defaultSets}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (doneSets >= exercise.defaultSets) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                for (setIdx in 1..exercise.defaultSets) {
                                    val completed = setIdx <= doneSets
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                onToggleSet(exercise.id, exercise.defaultSets, exercise.restSeconds)
                                            }
                                            .testTag("set_btn_${exercise.id}_$setIdx")
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            if (completed) {
                                                Icon(
                                                    Icons.Filled.Check,
                                                    contentDescription = "Set $setIdx completed",
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                            } else {
                                                Text("Set $setIdx", style = MaterialTheme.typography.labelMedium)
                                            }
                                        }
                                    }
                                }
                            }

                            if (exercise.proTips.isNotEmpty()) {
                                Text(
                                    text = "Cue: ${exercise.proTips.first()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    if (activeState.isWorkoutSaved) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Text("Workout Saved to Progress!", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    "Logged $totalCompletedSets sets in $elapsedFormatted.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                                    Text("Done")
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = onFinishWorkout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("finish_save_workout_button")
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Finish & Save Workout")
                        }
                    }
                }
            }
        }
    }
}
