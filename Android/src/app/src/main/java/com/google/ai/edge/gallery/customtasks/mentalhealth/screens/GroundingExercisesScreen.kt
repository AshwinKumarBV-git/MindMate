/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.edge.gallery.customtasks.mentalhealth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Grounding Exercises screen implementing the 5-4-3-2-1 technique.
 * No LLM processing required - purely static UI with interactive checklists.
 *
 * @param onNavigateBack Callback to navigate back to the dashboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroundingExercisesScreen(
  onNavigateBack: () -> Unit
) {
  // State for each sensory category
  var seeItems by remember { mutableStateOf(List(5) { false }) }
  var touchItems by remember { mutableStateOf(List(4) { false }) }
  var hearItems by remember { mutableStateOf(List(3) { false }) }
  var smellItems by remember { mutableStateOf(List(2) { false }) }
  var tasteItems by remember { mutableStateOf(List(1) { false }) }
  
  // Calculate progress
  val totalItems = 15 // 5 + 4 + 3 + 2 + 1
  val completedItems = seeItems.count { it } + 
                       touchItems.count { it } + 
                       hearItems.count { it } + 
                       smellItems.count { it } + 
                       tasteItems.count { it }
  val progressPercentage = (completedItems.toFloat() / totalItems * 100).toInt()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Grounding Exercises") },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        }
      )
    }
  ) { innerPadding ->

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Title
      Text(
        text = "5-4-3-2-1 Grounding Technique",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      
      Text(
        text = "Use your senses to reconnect with the present moment",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 16.dp)
      )
      
      // Progress indicator
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer
        )
      ) {
        Column(
          modifier = Modifier.padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Progress",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "$completedItems / $totalItems",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
          
          Spacer(modifier = Modifier.height(8.dp))
          
          LinearProgressIndicator(
            progress = { completedItems.toFloat() / totalItems },
            modifier = Modifier.fillMaxWidth(),
          )
          
          Text(
            text = "$progressPercentage% complete",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }
      
      Spacer(modifier = Modifier.height(24.dp))

      
      // 5 things you can see
      SensoryCategory(
        title = "5 things you can see",
        emoji = "👁️",
        items = seeItems,
        onItemToggle = { index ->
          seeItems = seeItems.toMutableList().apply { this[index] = !this[index] }
        }
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // 4 things you can touch
      SensoryCategory(
        title = "4 things you can touch",
        emoji = "✋",
        items = touchItems,
        onItemToggle = { index ->
          touchItems = touchItems.toMutableList().apply { this[index] = !this[index] }
        }
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // 3 things you can hear
      SensoryCategory(
        title = "3 things you can hear",
        emoji = "👂",
        items = hearItems,
        onItemToggle = { index ->
          hearItems = hearItems.toMutableList().apply { this[index] = !this[index] }
        }
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // 2 things you can smell
      SensoryCategory(
        title = "2 things you can smell",
        emoji = "👃",
        items = smellItems,
        onItemToggle = { index ->
          smellItems = smellItems.toMutableList().apply { this[index] = !this[index] }
        }
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // 1 thing you can taste
      SensoryCategory(
        title = "1 thing you can taste",
        emoji = "👅",
        items = tasteItems,
        onItemToggle = { index ->
          tasteItems = tasteItems.toMutableList().apply { this[index] = !this[index] }
        }
      )
      
      Spacer(modifier = Modifier.height(24.dp))
      
      // Reset button
      OutlinedButton(
        onClick = {
          seeItems = List(5) { false }
          touchItems = List(4) { false }
          hearItems = List(3) { false }
          smellItems = List(2) { false }
          tasteItems = List(1) { false }
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("Reset All")
      }
      
      // Completion message
      if (completedItems == totalItems) {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
          )
        ) {
          Text(
            text = "🎉 Great job! You've completed the grounding exercise.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
          )
        }
      }
    }
  }
}


/**
 * Composable for displaying a sensory category with checkboxes.
 *
 * @param title The category title (e.g., "5 things you can see")
 * @param emoji The emoji representing the sense
 * @param items List of boolean values representing checkbox states
 * @param onItemToggle Callback when a checkbox is toggled
 */
@Composable
private fun SensoryCategory(
  title: String,
  emoji: String,
  items: List<Boolean>,
  onItemToggle: (Int) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
      ) {
        Text(
          text = emoji,
          style = MaterialTheme.typography.headlineSmall,
          modifier = Modifier.padding(end = 8.dp)
        )
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      
      items.forEachIndexed { index, isChecked ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = isChecked,
            onCheckedChange = { onItemToggle(index) }
          )
          Text(
            text = "Item ${index + 1}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
          )
        }
      }
    }
  }
}
