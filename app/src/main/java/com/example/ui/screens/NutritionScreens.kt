package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.catalog.DietPlanMeal
import com.example.data.catalog.FoodItem
import com.example.data.catalog.WellnessCatalog
import com.example.ui.components.CircularMetricRing
import com.example.ui.components.FitPulseTopBar
import com.example.ui.components.TagChip
import com.example.ui.theme.AccentDiet
import com.example.ui.theme.AccentNutrition
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.EmeraldBright
import com.example.ui.viewmodel.FitPulseUiState

@Composable
fun DietHubScreen(
    state: FitPulseUiState,
    onNavigate: (String) -> Unit,
    onLogFood: (FoodItem) -> Unit
) {
    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Diet & Nutrition",
                subtitle = "${state.todayCaloriesIn} / ${state.userProfile.dailyCalorieGoal} kcal today"
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
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = Color(0xFF101C18),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_hero_nutrition),
                                contentDescription = "Healthy athlete meal bowl",
                                contentScale = ContentScale.Crop,
                                alpha = 0.4f,
                                modifier = Modifier.matchParentSize()
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF0A1416).copy(alpha = 0.92f),
                                                Color(0xFF0A1416).copy(alpha = 0.65f)
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "DAILY FUEL & MACROS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldBright,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${state.todayCaloriesIn} / ${state.userProfile.dailyCalorieGoal} kcal",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "Protein ${state.todayProteinG}g/${state.userProfile.dailyProteinGoalG}g  •  Carbs ${state.todayCarbsG}g  •  Fats ${state.todayFatsG}g",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFD0DFDA)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(
                                        onClick = { onNavigate("nutrition") },
                                        modifier = Modifier.testTag("diet_hub_open_nutrition")
                                    ) {
                                        Text("Calorie & Macro Log")
                                    }
                                    OutlinedButton(
                                        onClick = { onNavigate("diet_plan") },
                                        modifier = Modifier.testTag("diet_hub_open_plan")
                                    ) {
                                        Text("Meal Plans", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DietHubFeatureCard(
                            title = "Diet Plan",
                            subtitle = "4 structured daily meal plans",
                            icon = Icons.Filled.RestaurantMenu,
                            accent = AccentDiet,
                            onClick = { onNavigate("diet_plan") },
                            modifier = Modifier.weight(1f)
                        )
                        DietHubFeatureCard(
                            title = "Nutrition",
                            subtitle = "Food database & custom log",
                            icon = Icons.Filled.Restaurant,
                            accent = AccentNutrition,
                            onClick = { onNavigate("nutrition") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Quick-Log Popular Athlete Meals", style = MaterialTheme.typography.headlineSmall)
                        TextButton(onClick = { onNavigate("nutrition") }) { Text("See All") }
                    }
                }

                items(WellnessCatalog.foods.take(6), key = { it.id }) { food ->
                    FoodQuickLogRow(food = food, onLog = { onLogFood(food) })
                }
            }
        }
    }
}

@Composable
private fun DietHubFeatureCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.14f))
            ) {
                Icon(icon, contentDescription = title, tint = accent)
            }
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Open", style = MaterialTheme.typography.labelMedium, color = accent)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = accent, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
fun DietPlanScreen(
    onBack: () -> Unit,
    onLogPlanMeal: (DietPlanMeal) -> Unit,
    onOpenNutrition: () -> Unit
) {
    var selectedPlanIndex by remember { mutableStateOf(0) }
    val selectedPlan = WellnessCatalog.dietPlans[selectedPlanIndex]
    var lastLoggedMealTitle by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Diet Plans",
                subtitle = "Structured nutrition templates",
                onBack = onBack,
                actions = {
                    TextButton(onClick = onOpenNutrition) { Text("Today's Log") }
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
                    .widthIn(max = 1000.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(WellnessCatalog.dietPlans.size) { index ->
                            val plan = WellnessCatalog.dietPlans[index]
                            FilterChip(
                                selected = selectedPlanIndex == index,
                                onClick = { selectedPlanIndex = index },
                                label = { Text(plan.name) },
                                modifier = Modifier.testTag("diet_plan_tab_$index")
                            )
                        }
                    }
                }

                item {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TagChip(selectedPlan.goalBadge, MaterialTheme.colorScheme.primary)
                            Text(selectedPlan.name, style = MaterialTheme.typography.headlineMedium)
                            Text(selectedPlan.description, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "${selectedPlan.targetCalories} kcal/day  •  ${selectedPlan.macroSplit}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (lastLoggedMealTitle != null) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Logged \"$lastLoggedMealTitle\" to Today's Nutrition!", style = MaterialTheme.typography.bodySmall)
                                TextButton(onClick = onOpenNutrition) { Text("View") }
                            }
                        }
                    }
                }

                items(selectedPlan.meals) { meal ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
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
                                TagChip(meal.mealType, AccentNutrition)
                                Text("${meal.calories} kcal", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(meal.title, style = MaterialTheme.typography.titleMedium)
                            Text(meal.itemsSummary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("P: ${meal.proteinG}g  •  C: ${meal.carbsG}g  •  F: ${meal.fatsG}g", style = MaterialTheme.typography.labelMedium)
                                Button(
                                    onClick = {
                                        onLogPlanMeal(meal)
                                        lastLoggedMealTitle = meal.title
                                    },
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Log Meal")
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
fun NutritionScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onLogFood: (FoodItem) -> Unit,
    onLogCustomMeal: (name: String, mealType: String, calories: Int, protein: Int, carbs: Int, fats: Int) -> Unit,
    onDeleteMeal: (Long) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("All") }
    var showCustomMealDialog by remember { mutableStateOf(false) }

    val mealTypes = listOf("All", "Breakfast", "Lunch", "Dinner", "Snack")
    val filteredFoods = remember(search, selectedMealType) {
        WellnessCatalog.foods.filter { food ->
            val matchType = selectedMealType == "All" || food.mealType.equals(selectedMealType, ignoreCase = true)
            val q = search.trim().lowercase()
            val matchQuery = q.isEmpty() || food.name.lowercase().contains(q) || food.tags.lowercase().contains(q)
            matchType && matchQuery
        }
    }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Nutrition Tracker",
                subtitle = "${state.todayCaloriesIn} / ${state.userProfile.dailyCalorieGoal} kcal",
                onBack = onBack,
                actions = {
                    TextButton(
                        onClick = { showCustomMealDialog = true },
                        modifier = Modifier.testTag("nutrition_custom_meal_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Custom")
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
                    .widthIn(max = 1000.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Calorie & Macro Breakdown Card
                item {
                    val calGoal = state.userProfile.dailyCalorieGoal.coerceAtLeast(1)
                    val remaining = (calGoal - state.todayCaloriesIn).coerceAtLeast(0)
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
                                CircularMetricRing(
                                    progress = state.todayCaloriesIn.toFloat() / calGoal,
                                    color = CoralAccent,
                                    size = 82.dp,
                                    strokeWidth = 8.dp,
                                    centerLabel = "${state.todayCaloriesIn}",
                                    centerSubLabel = "kcal"
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("$remaining kcal remaining of $calGoal kcal", style = MaterialTheme.typography.titleMedium)
                                    MacroProgressBar("Protein", state.todayProteinG, state.userProfile.dailyProteinGoalG, Color(0xFF00A870))
                                    MacroProgressBar("Carbs", state.todayCarbsG, state.userProfile.dailyCarbsGoalG, Color(0xFF0EA5E9))
                                    MacroProgressBar("Fats", state.todayFatsG, state.userProfile.dailyFatsGoalG, Color(0xFFF59E0B))
                                }
                            }
                        }
                    }
                }

                // Today's Logged Meals
                item {
                    Text("Logged Meals (${state.mealLogs.size})", style = MaterialTheme.typography.headlineSmall)
                }

                if (state.mealLogs.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No meals logged yet today. Tap + on any food below or add a custom meal!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                } else {
                    items(state.mealLogs.take(12), key = { it.id }) { meal ->
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
                                    Text(meal.mealType.uppercase(), style = MaterialTheme.typography.labelSmall, color = AccentNutrition)
                                    Text(meal.name, style = MaterialTheme.typography.titleSmall)
                                    Text(
                                        "${meal.calories} kcal • P ${meal.proteinG}g • C ${meal.carbsG}g • F ${meal.fatsG}g",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { onDeleteMeal(meal.id) }) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = "Remove meal")
                                }
                            }
                        }
                    }
                }

                // Searchable Food Database
                item {
                    Text("Food Database", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        placeholder = { Text("Search foods, protein, snacks...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        trailingIcon = {
                            if (search.isNotEmpty()) {
                                IconButton(onClick = { search = "" }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("nutrition_food_search")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(mealTypes) { type ->
                            FilterChip(
                                selected = selectedMealType == type,
                                onClick = { selectedMealType = type },
                                label = { Text(type) }
                            )
                        }
                    }
                }

                items(filteredFoods, key = { it.id }) { food ->
                    FoodQuickLogRow(food = food, onLog = { onLogFood(food) })
                }
            }
        }
    }

    if (showCustomMealDialog) {
        CustomMealDialog(
            onDismiss = { showCustomMealDialog = false },
            onSave = { name, type, cal, p, c, f ->
                onLogCustomMeal(name, type, cal, p, c, f)
                showCustomMealDialog = false
            }
        )
    }
}

@Composable
private fun MacroProgressBar(label: String, current: Int, goal: Int, color: Color) {
    val ratio = (current.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text("${current}g / ${goal}g", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        LinearProgressIndicator(
            progress = { ratio },
            color = color,
            trackColor = color.copy(alpha = 0.16f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun FoodQuickLogRow(food: FoodItem, onLog: () -> Unit) {
    var justAdded by remember { mutableStateOf(false) }
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
                Text("${food.mealType.uppercase()} • ${food.tags}", style = MaterialTheme.typography.labelSmall, color = AccentNutrition)
                Text(food.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    "${food.calories} kcal • P ${food.proteinG}g • C ${food.carbsG}g • F ${food.fatsG}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = {
                    onLog()
                    justAdded = true
                },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("log_food_${food.id}")
            ) {
                Icon(
                    imageVector = if (justAdded) Icons.Filled.Check else Icons.Filled.Add,
                    contentDescription = "Log ${food.name}",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (justAdded) "Added" else "Log")
            }
        }
    }
}

@Composable
private fun CustomMealDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Int, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf("Lunch") }
    var calories by remember { mutableStateOf("450") }
    var protein by remember { mutableStateOf("35") }
    var carbs by remember { mutableStateOf("45") }
    var fats by remember { mutableStateOf("12") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Custom Meal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Meal Name") },
                    singleLine = true
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("Breakfast", "Lunch", "Dinner", "Snack")) { type ->
                        FilterChip(
                            selected = mealType == type,
                            onClick = { mealType = type },
                            label = { Text(type) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it.filter { c -> c.isDigit() } },
                        label = { Text("kcal") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it.filter { c -> c.isDigit() } },
                        label = { Text("Prot g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it.filter { c -> c.isDigit() } },
                        label = { Text("Carb g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fats,
                        onValueChange = { fats = it.filter { c -> c.isDigit() } },
                        label = { Text("Fat g") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            name,
                            mealType,
                            calories.toIntOrNull() ?: 300,
                            protein.toIntOrNull() ?: 20,
                            carbs.toIntOrNull() ?: 30,
                            fats.toIntOrNull() ?: 10
                        )
                    }
                }
            ) { Text("Save Meal") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
