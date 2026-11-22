package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.catalog.MuscleGroup
import com.example.ui.components.CircularMetricRing
import com.example.ui.components.FitPulseTopBar
import com.example.ui.theme.AccentProgress
import com.example.ui.theme.AccentSleep
import com.example.ui.theme.AccentWater
import com.example.ui.theme.AccentWeight
import com.example.ui.theme.CoralAccent
import com.example.ui.viewmodel.FitPulseUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.pow

@Composable
fun WaterTrackerScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onAddWater: (Int) -> Unit,
    onDeleteWater: (Long) -> Unit,
    onUpdateGoal: (Int) -> Unit
) {
    var customMl by remember { mutableStateOf("350") }
    var showGoalDialog by remember { mutableStateOf(false) }

    val goalMl = state.userProfile.dailyWaterGoalMl.coerceAtLeast(1000)
    val progress = (state.todayWaterMl.toFloat() / goalMl).coerceIn(0f, 1f)
    val remaining = (goalMl - state.todayWaterMl).coerceAtLeast(0)

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Water Tracker",
                subtitle = "Goal: $goalMl ml daily",
                onBack = onBack,
                actions = {
                    TextButton(onClick = { showGoalDialog = true }) { Text("Goal") }
                }
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            CircularMetricRing(
                                progress = progress,
                                color = AccentWater,
                                size = 140.dp,
                                strokeWidth = 12.dp,
                                centerLabel = "${state.todayWaterMl} ml",
                                centerSubLabel = "${(progress * 100).toInt()}%"
                            )
                            Text(
                                text = if (remaining == 0) "Daily hydration goal reached!" else "$remaining ml left to reach your target",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(150, 250, 500, 750).forEach { ml ->
                                    Button(
                                        onClick = { onAddWater(ml) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AccentWater),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("water_add_$ml")
                                    ) {
                                        Text("+${ml}ml", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = customMl,
                                    onValueChange = { customMl = it.filter { c -> c.isDigit() } },
                                    label = { Text("Custom ml") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = {
                                        val amount = customMl.toIntOrNull() ?: 0
                                        if (amount > 0) onAddWater(amount)
                                    },
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add")
                                }
                            }
                        }
                    }
                }

                item {
                    Text("Hydration History", style = MaterialTheme.typography.headlineSmall)
                }

                if (state.waterLogs.isEmpty()) {
                    item {
                        EmptyTrackerCard(
                            icon = Icons.Filled.LocalDrink,
                            title = "No hydration logged yet",
                            tip = "Tap +250ml or +500ml above whenever you drink water."
                        )
                    }
                } else {
                    items(state.waterLogs.take(15), key = { it.id }) { log ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Filled.LocalDrink, contentDescription = null, tint = AccentWater)
                                    Column {
                                        Text("${log.amountMl} ml", style = MaterialTheme.typography.titleMedium)
                                        Text(formatTimestamp(log.timestamp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                IconButton(onClick = { onDeleteWater(log.id) }) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete water entry")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showGoalDialog) {
        var goalInput by remember { mutableStateOf(goalMl.toString()) }
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text("Daily Water Goal (ml)") },
            text = {
                OutlinedTextField(
                    value = goalInput,
                    onValueChange = { goalInput = it.filter { c -> c.isDigit() } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    goalInput.toIntOrNull()?.let(onUpdateGoal)
                    showGoalDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun SleepTrackerScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onAddSleep: (hours: Float, quality: Int, bedtime: String, wakeTime: String, note: String) -> Unit,
    onDeleteSleep: (Long) -> Unit
) {
    var hoursSlider by remember { mutableFloatStateOf(7.5f) }
    var qualityRating by remember { mutableIntStateOf(4) }
    var bedtime by remember { mutableStateOf("10:45 PM") }
    var wakeTime by remember { mutableStateOf("6:15 AM") }
    var note by remember { mutableStateOf("") }

    val goalHours = state.userProfile.dailySleepGoalHours
    val avgHours = if (state.sleepLogs.isEmpty()) 0f else state.sleepLogs.take(7).map { it.hoursSlept }.average().toFloat()

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Sleep Tracker",
                subtitle = "Target: ${"%.1f".format(goalHours)} hrs/night",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("7-Day Average", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = if (state.sleepLogs.isEmpty()) "-- hrs" else "${"%.1f".format(avgHours)} hrs",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = AccentSleep
                                    )
                                }
                                CircularMetricRing(
                                    progress = avgHours / goalHours.coerceAtLeast(1f),
                                    color = AccentSleep,
                                    size = 72.dp,
                                    strokeWidth = 7.dp,
                                    centerLabel = state.latestSleep?.let { "${"%.1f".format(it.hoursSlept)}h" } ?: "--"
                                )
                            }

                            if (state.sleepLogs.isNotEmpty()) {
                                val chartValues = state.sleepLogs.take(7).reversed().map { it.hoursSlept }
                                Text("Recent Nights (hrs)", style = MaterialTheme.typography.labelMedium)
                                SimpleBarChartCanvas(
                                    values = chartValues,
                                    maxValue = 11f,
                                    barColor = AccentSleep,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                )
                            }
                        }
                    }
                }

                // Log Sleep Form
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Log Sleep Session", style = MaterialTheme.typography.titleLarge)
                            Text("Duration: ${"%.1f".format(hoursSlider)} hours", style = MaterialTheme.typography.titleMedium, color = AccentSleep)
                            Slider(
                                value = hoursSlider,
                                onValueChange = { hoursSlider = (it * 2).toInt() / 2f },
                                valueRange = 3f..12f
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("Sleep Quality:", style = MaterialTheme.typography.bodyMedium)
                                (1..5).forEach { star ->
                                    IconButton(onClick = { qualityRating = star }) {
                                        Icon(
                                            imageVector = if (star <= qualityRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                                            contentDescription = "Quality $star stars",
                                            tint = Color(0xFFF59E0B)
                                        )
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = bedtime,
                                    onValueChange = { bedtime = it },
                                    label = { Text("Bedtime") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = wakeTime,
                                    onValueChange = { wakeTime = it },
                                    label = { Text("Wake Time") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            OutlinedTextField(
                                value = note,
                                onValueChange = { note = it },
                                label = { Text("Recovery note (optional)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    onAddSleep(hoursSlider, qualityRating, bedtime, wakeTime, note)
                                    note = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentSleep),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("sleep_save_button")
                            ) {
                                Text("Save Sleep Log")
                            }
                        }
                    }
                }

                item {
                    Text("Sleep History", style = MaterialTheme.typography.headlineSmall)
                }

                if (state.sleepLogs.isEmpty()) {
                    item {
                        EmptyTrackerCard(
                            icon = Icons.Filled.Bedtime,
                            title = "No sleep nights logged yet",
                            tip = "Log last night's sleep above to monitor recovery consistency."
                        )
                    }
                } else {
                    items(state.sleepLogs.take(14), key = { it.id }) { log ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${"%.1f".format(log.hoursSlept)} hrs • ${log.qualityRating}/5 ★", style = MaterialTheme.typography.titleMedium)
                                    Text("${log.bedtime} -> ${log.wakeTime}  •  ${formatTimestamp(log.timestamp)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (log.note.isNotBlank()) {
                                        Text(log.note, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                IconButton(onClick = { onDeleteSleep(log.id) }) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete sleep log")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeightManagementScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onAddWeight: (weightKg: Float, bodyFat: Float?, note: String) -> Unit,
    onDeleteWeight: (Long) -> Unit
) {
    var showLogDialog by remember { mutableStateOf(false) }

    val profile = state.userProfile
    val currentKg = profile.currentWeightKg
    val targetKg = profile.targetWeightKg
    val heightM = (profile.heightCm / 100f).coerceAtLeast(1.0f)
    val bmi = currentKg / heightM.pow(2)
    val bmiCategory = when {
        bmi < 18.5f -> "Underweight"
        bmi < 25f -> "Healthy Range"
        bmi < 30f -> "Overweight"
        else -> "Obese Range"
    }
    val bmr = (10f * currentKg + 6.25f * profile.heightCm - 5f * profile.age + 5f).toInt()
    val activityMultiplier = when {
        profile.activityLevel.contains("Light") -> 1.375f
        profile.activityLevel.contains("High") -> 1.725f
        else -> 1.55f
    }
    val tdee = (bmr * activityMultiplier).toInt()
    val diffKg = currentKg - targetKg

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Weight Management",
                subtitle = "BMI, TDEE & Trend Tracking",
                onBack = onBack,
                actions = {
                    TextButton(
                        onClick = { showLogDialog = true },
                        modifier = Modifier.testTag("weight_add_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Log")
                    }
                }
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Current Weight", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${"%.1f".format(currentKg)} kg", style = MaterialTheme.typography.displaySmall, color = AccentWeight)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Target Weight", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${"%.1f".format(targetKg)} kg", style = MaterialTheme.typography.headlineMedium)
                                }
                            }
                            Text(
                                text = when {
                                    kotlin.math.abs(diffKg) < 0.3f -> "Right on your target weight!"
                                    diffKg > 0 -> "${"%.1f".format(diffKg)} kg to lose • Suggested intake: ${(tdee - 400).coerceAtLeast(1400)} kcal/day"
                                    else -> "${"%.1f".format(-diffKg)} kg to gain • Suggested intake: ${tdee + 300} kcal/day"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                MetricPillBox("BMI", "${"%.1f".format(bmi)} ($bmiCategory)", Modifier.weight(1f))
                                MetricPillBox("Est. TDEE", "$tdee kcal", Modifier.weight(1f))
                            }
                        }
                    }
                }

                if (state.weightLogs.size >= 2) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Weight Trend (kg)", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(10.dp))
                                SimpleLineChartCanvas(
                                    values = state.weightLogs.take(12).reversed().map { it.weightKg },
                                    lineColor = AccentWeight,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { showLogDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentWeight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.MonitorWeight, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Today's Weight")
                    }
                }

                item {
                    Text("Weight Check-ins", style = MaterialTheme.typography.headlineSmall)
                }

                if (state.weightLogs.isEmpty()) {
                    item {
                        EmptyTrackerCard(
                            icon = Icons.Filled.MonitorWeight,
                            title = "No weight check-ins yet",
                            tip = "Log your morning weight regularly to plot your trend line."
                        )
                    }
                } else {
                    items(state.weightLogs, key = { it.id }) { log ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${"%.1f".format(log.weightKg)} kg" + (log.bodyFatPercent?.let { " • ${"%.1f".format(it)}% BF" } ?: ""),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(formatTimestamp(log.timestamp) + if (log.note.isNotBlank()) " • ${log.note}" else "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { onDeleteWeight(log.id) }) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete weight entry")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showLogDialog) {
        var weightStr by remember { mutableStateOf("%.1f".format(currentKg)) }
        var bfStr by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showLogDialog = false },
            title = { Text("Log Weight") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightStr,
                        onValueChange = { weightStr = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bfStr,
                        onValueChange = { bfStr = it },
                        label = { Text("Body Fat % (optional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Note (optional)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val w = weightStr.toFloatOrNull()
                    if (w != null && w > 20f) {
                        onAddWeight(w, bfStr.toFloatOrNull(), note)
                        showLogDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showLogDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ProgressTrackerScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onNavigateWorkout: () -> Unit
) {
    val totalWorkouts = state.workoutLogs.size
    val totalSets = state.workoutLogs.sumOf { it.setsCompleted }
    val totalCalories = state.workoutLogs.sumOf { it.caloriesBurned }
    val totalMinutes = state.workoutLogs.sumOf { it.durationMinutes }

    val muscleCounts = remember(state.workoutLogs) {
        MuscleGroup.entries.map { group ->
            group to state.workoutLogs.count { it.muscleGroup.equals(group.displayName, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Progress Tracker",
                subtitle = "Training volume, balance & milestones",
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
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricPillBox("Workouts", "$totalWorkouts", Modifier.weight(1f))
                        MetricPillBox("Total Sets", "$totalSets", Modifier.weight(1f))
                        MetricPillBox("Burned", "$totalCalories kcal", Modifier.weight(1f))
                        MetricPillBox("Minutes", "${totalMinutes}m", Modifier.weight(1f))
                    }
                }

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
                            Text("Recent Workout Calorie Output", style = MaterialTheme.typography.titleMedium)
                            if (state.workoutLogs.isEmpty()) {
                                Text(
                                    "Complete or log workouts to populate your output chart.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                SimpleBarChartCanvas(
                                    values = state.workoutLogs.take(8).reversed().map { it.caloriesBurned.toFloat() },
                                    maxValue = (state.workoutLogs.maxOfOrNull { it.caloriesBurned } ?: 300).coerceAtLeast(200).toFloat(),
                                    barColor = AccentProgress,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                )
                            }
                        }
                    }
                }

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
                            Text("Muscle Split Distribution", style = MaterialTheme.typography.titleMedium)
                            val maxCount = muscleCounts.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1
                            muscleCounts.forEach { (group, count) ->
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(group.displayName, style = MaterialTheme.typography.labelMedium)
                                        Text("$count sessions", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    LinearProgressIndicator(
                                        progress = { count.toFloat() / maxCount },
                                        color = group.accentColor,
                                        trackColor = group.accentColor.copy(alpha = 0.14f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(7.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Logged Sessions", style = MaterialTheme.typography.headlineSmall)
                        OutlinedButton(onClick = onNavigateWorkout) { Text("New Workout") }
                    }
                }

                if (state.workoutLogs.isEmpty()) {
                    item {
                        EmptyTrackerCard(
                            icon = Icons.Filled.Timeline,
                            title = "Your timeline is ready",
                            tip = "Finish any muscle split workout or log a custom session to watch your progress climb."
                        )
                    }
                } else {
                    items(state.workoutLogs, key = { it.id }) { log ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(log.title, style = MaterialTheme.typography.titleMedium)
                                    Text("${log.caloriesBurned} kcal", style = MaterialTheme.typography.labelLarge, color = CoralAccent)
                                }
                                Text(
                                    "${log.muscleGroup} • ${log.setsCompleted} sets • ${log.durationMinutes} min • ${formatTimestamp(log.timestamp)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricPillBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun EmptyTrackerCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tip: String
) {
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
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(34.dp))
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(tip, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SimpleBarChartCanvas(
    values: List<Float>,
    maxValue: Float,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (values.isEmpty()) return@Canvas
        val count = values.size
        val slotWidth = size.width / count
        val barWidth = (slotWidth * 0.52f).coerceAtMost(48f)
        values.forEachIndexed { idx, value ->
            val ratio = (value / maxValue.coerceAtLeast(1f)).coerceIn(0.05f, 1f)
            val barHeight = size.height * ratio
            val left = idx * slotWidth + (slotWidth - barWidth) / 2f
            val top = size.height - barHeight
            drawRoundRect(
                color = barColor.copy(alpha = 0.16f),
                topLeft = Offset(left, 0f),
                size = Size(barWidth, size.height),
                cornerRadius = CornerRadius(12f, 12f)
            )
            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(12f, 12f)
            )
        }
    }
}

@Composable
fun SimpleLineChartCanvas(
    values: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (values.size < 2) return@Canvas
        val minVal = (values.minOrNull() ?: 0f) - 1f
        val maxVal = (values.maxOrNull() ?: 100f) + 1f
        val span = (maxVal - minVal).coerceAtLeast(1f)
        val stepX = size.width / (values.size - 1)

        val path = Path()
        values.forEachIndexed { index, value ->
            val x = index * stepX
            val y = size.height - ((value - minVal) / span) * (size.height * 0.85f) - size.height * 0.08f
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
        values.forEachIndexed { index, value ->
            val x = index * stepX
            val y = size.height - ((value - minVal) / span) * (size.height * 0.85f) - size.height * 0.08f
            drawCircle(color = lineColor, radius = 8f, center = Offset(x, y))
        }
    }
}

fun formatTimestamp(millis: Long): String {
    val sdf = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    return sdf.format(Date(millis))
}
