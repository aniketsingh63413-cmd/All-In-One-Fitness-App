package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import com.example.data.local.CommunityPostEntity
import com.example.data.local.UserProfile
import com.example.ui.components.FitPulseTopBar
import com.example.ui.components.TagChip
import com.example.ui.theme.AccentCommunity
import com.example.ui.viewmodel.FitPulseUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    state: FitPulseUiState,
    onBack: () -> Unit,
    onCreatePost: (category: String, title: String, body: String) -> Unit,
    onToggleLike: (CommunityPostEntity) -> Unit,
    onToggleBookmark: (CommunityPostEntity) -> Unit,
    onAddComment: (CommunityPostEntity, String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showComposer by remember { mutableStateOf(false) }
    var commentingPost by remember { mutableStateOf<CommunityPostEntity?>(null) }

    val categories = listOf("All", "Milestone", "Workout", "Nutrition", "Question")
    val filteredPosts = remember(state.communityPosts, selectedCategory) {
        if (selectedCategory == "All") state.communityPosts
        else state.communityPosts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Community",
                subtitle = "Local athlete board • ${state.communityPosts.size} posts",
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showComposer = true },
                modifier = Modifier.testTag("community_new_post_fab")
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create post")
            }
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat) }
                            )
                        }
                    }
                }

                items(filteredPosts, key = { it.id }) { post ->
                    val commentsList = remember(post.commentsText) {
                        post.commentsText.lines().filter { it.isNotBlank() }
                    }
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(AccentCommunity.copy(alpha = 0.16f))
                                    ) {
                                        Text(
                                            text = post.authorName.take(1).uppercase(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = AccentCommunity
                                        )
                                    }
                                    Column {
                                        Text(post.authorName, style = MaterialTheme.typography.titleSmall)
                                        Text("${post.authorHandle} • ${formatTimestamp(post.timestamp)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                TagChip(post.category, AccentCommunity)
                            }
                            Text(post.title, style = MaterialTheme.typography.titleMedium)
                            Text(post.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(
                                        onClick = { onToggleLike(post) },
                                        modifier = Modifier.testTag("post_like_${post.id}")
                                    ) {
                                        Icon(
                                            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                            contentDescription = "Like post",
                                            tint = if (post.isLiked) AccentCommunity else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${post.likesCount}")
                                    }
                                    TextButton(
                                        onClick = { commentingPost = post },
                                        modifier = Modifier.testTag("post_comment_${post.id}")
                                    ) {
                                        Icon(Icons.Filled.ChatBubbleOutline, contentDescription = "Comments", modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${commentsList.size}")
                                    }
                                }
                                IconButton(onClick = { onToggleBookmark(post) }) {
                                    Icon(
                                        imageVector = if (post.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                        contentDescription = "Save post"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showComposer) {
        var category by remember { mutableStateOf("Milestone") }
        var title by remember { mutableStateOf("") }
        var body by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showComposer = false },
            title = { Text("Share with Community") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf("Milestone", "Workout", "Nutrition", "Question")) { cat ->
                            FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) })
                        }
                    }
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Headline") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = body,
                        onValueChange = { body = it },
                        label = { Text("Share your workout, meal, or tip") },
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank() && body.isNotBlank()) {
                        onCreatePost(category, title, body)
                        showComposer = false
                    }
                }) { Text("Post") }
            },
            dismissButton = {
                TextButton(onClick = { showComposer = false }) { Text("Cancel") }
            }
        )
    }

    commentingPost?.let { activePost ->
        val freshPost = state.communityPosts.find { it.id == activePost.id } ?: activePost
        val comments = freshPost.commentsText.lines().filter { it.isNotBlank() }
        var newComment by remember { mutableStateOf("") }

        ModalBottomSheet(onDismissRequest = { commentingPost = null }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Comments (${comments.size})", style = MaterialTheme.typography.titleLarge)
                if (comments.isEmpty()) {
                    Text("Be the first to reply!", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    comments.forEach { line ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(line, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        placeholder = { Text("Write a comment...") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (newComment.isNotBlank()) {
                                onAddComment(freshPost, newComment)
                                newComment = ""
                            }
                        }
                    ) { Text("Send") }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    state: FitPulseUiState,
    onSaveProfile: (UserProfile) -> Unit,
    onNavigate: (String) -> Unit
) {
    val current = state.userProfile
    var name by remember(current.name) { mutableStateOf(current.name) }
    var age by remember(current.age) { mutableStateOf(current.age.toString()) }
    var heightCm by remember(current.heightCm) { mutableStateOf(current.heightCm.toString()) }
    var weightKg by remember(current.currentWeightKg) { mutableStateOf("%.1f".format(current.currentWeightKg)) }
    var targetWeightKg by remember(current.targetWeightKg) { mutableStateOf("%.1f".format(current.targetWeightKg)) }
    var calGoal by remember(current.dailyCalorieGoal) { mutableStateOf(current.dailyCalorieGoal.toString()) }
    var proteinGoal by remember(current.dailyProteinGoalG) { mutableStateOf(current.dailyProteinGoalG.toString()) }
    var waterGoal by remember(current.dailyWaterGoalMl) { mutableStateOf(current.dailyWaterGoalMl.toString()) }
    var sleepGoal by remember(current.dailySleepGoalHours) { mutableStateOf("%.1f".format(current.dailySleepGoalHours)) }
    var fitnessGoal by remember(current.fitnessGoal) { mutableStateOf(current.fitnessGoal) }
    var darkOverride by remember(current.darkThemeOverride) { mutableStateOf(current.darkThemeOverride) }
    var savedToast by remember { mutableStateOf(false) }

    val goalOptions = listOf(
        "Lean Muscle & Strength",
        "Fat Loss & Toning",
        "Athletic Endurance",
        "General Health & Mobility"
    )

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "Profile & Goals",
                subtitle = "${state.workoutLogs.size} workouts • ${state.mealLogs.size} meals logged"
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onNavigate("progress_tracker") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.Timeline, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Progress")
                        }
                        OutlinedButton(
                            onClick = { onNavigate("all_features") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.GridView, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("All Features")
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
                            Text("Personal Details", style = MaterialTheme.typography.titleLarge)
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Display Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = age,
                                    onValueChange = { age = it.filter { c -> c.isDigit() } },
                                    label = { Text("Age") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = heightCm,
                                    onValueChange = { heightCm = it.filter { c -> c.isDigit() } },
                                    label = { Text("Height (cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = weightKg,
                                    onValueChange = { weightKg = it },
                                    label = { Text("Current (kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = targetWeightKg,
                                    onValueChange = { targetWeightKg = it },
                                    label = { Text("Target (kg)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Text("Fitness Focus", style = MaterialTheme.typography.labelLarge)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(goalOptions) { option ->
                                    FilterChip(
                                        selected = fitnessGoal == option,
                                        onClick = { fitnessGoal = option },
                                        label = { Text(option) }
                                    )
                                }
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
                            Text("Daily Targets & Appearance", style = MaterialTheme.typography.titleLarge)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = calGoal,
                                    onValueChange = { calGoal = it.filter { c -> c.isDigit() } },
                                    label = { Text("Calories (kcal)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = proteinGoal,
                                    onValueChange = { proteinGoal = it.filter { c -> c.isDigit() } },
                                    label = { Text("Protein (g)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = waterGoal,
                                    onValueChange = { waterGoal = it.filter { c -> c.isDigit() } },
                                    label = { Text("Water (ml)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = sleepGoal,
                                    onValueChange = { sleepGoal = it },
                                    label = { Text("Sleep (hrs)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Text("App Theme", style = MaterialTheme.typography.labelLarge)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(selected = darkOverride == null, onClick = { darkOverride = null }, label = { Text("System") })
                                FilterChip(selected = darkOverride == false, onClick = { darkOverride = false }, label = { Text("Light") })
                                FilterChip(selected = darkOverride == true, onClick = { darkOverride = true }, label = { Text("Dark") })
                            }
                        }
                    }
                }

                item {
                    if (savedToast) {
                        Text(
                            text = "Profile and daily goals saved!",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            onSaveProfile(
                                current.copy(
                                    name = name.ifBlank { "Athlete" },
                                    age = (age.toIntOrNull() ?: 27).coerceIn(13, 100),
                                    heightCm = (heightCm.toIntOrNull() ?: 175).coerceIn(100, 240),
                                    currentWeightKg = (weightKg.toFloatOrNull() ?: current.currentWeightKg).coerceIn(30f, 300f),
                                    targetWeightKg = (targetWeightKg.toFloatOrNull() ?: current.targetWeightKg).coerceIn(30f, 300f),
                                    dailyCalorieGoal = (calGoal.toIntOrNull() ?: 2300).coerceIn(1000, 6000),
                                    dailyProteinGoalG = (proteinGoal.toIntOrNull() ?: 160).coerceIn(40, 400),
                                    dailyWaterGoalMl = (waterGoal.toIntOrNull() ?: 3000).coerceIn(1000, 8000),
                                    dailySleepGoalHours = (sleepGoal.toFloatOrNull() ?: 8f).coerceIn(5f, 12f),
                                    fitnessGoal = fitnessGoal,
                                    darkThemeOverride = darkOverride
                                )
                            )
                            savedToast = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("profile_save_button")
                    ) {
                        Text("Save Profile & Goals")
                    }
                }
            }
        }
    }
}
