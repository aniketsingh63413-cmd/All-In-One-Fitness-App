package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.catalog.WellnessCatalog
import com.example.ui.components.FeatureCardTile
import com.example.ui.components.FitPulseTopBar
import com.example.ui.components.featureVisuals

@Composable
fun AllFeaturesScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember {
        listOf("All") + WellnessCatalog.allFeatures.map { it.category }.distinct()
    }

    val filtered = remember(query, selectedCategory) {
        WellnessCatalog.allFeatures.filter { feat ->
            val matchesCat = selectedCategory == "All" || feat.category == selectedCategory
            val q = query.trim().lowercase()
            val matchesQuery = q.isEmpty() ||
                feat.title.lowercase().contains(q) ||
                feat.subtitle.lowercase().contains(q) ||
                feat.category.lowercase().contains(q)
            matchesCat && matchesQuery
        }
    }

    Scaffold(
        topBar = {
            FitPulseTopBar(
                title = "All Features",
                subtitle = "${WellnessCatalog.allFeatures.size} tools, splits & trackers",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val columns = when {
                maxWidth >= 900.dp -> 4
                maxWidth >= 600.dp -> 3
                else -> 2
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1100.dp)
                    .align(Alignment.TopCenter),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Filter features...") },
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
                            .testTag("all_features_search")
                    )
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category) }
                            )
                        }
                    }
                }

                item {
                    AdaptiveGridRows(columns = columns, items = filtered) { feat ->
                        val (icon, color) = featureVisuals(feat.id)
                        FeatureCardTile(
                            title = feat.title,
                            subtitle = feat.subtitle,
                            icon = icon,
                            accent = color,
                            tag = "all_feat_${feat.id}",
                            badge = feat.category,
                            onClick = { onNavigate(feat.route) }
                        )
                    }
                }
            }
        }
    }
}
