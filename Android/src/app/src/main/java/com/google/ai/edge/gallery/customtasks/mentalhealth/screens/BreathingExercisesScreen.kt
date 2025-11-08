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

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Breathing exercise types with their timing patterns.
 */
enum class BreathingExercise(
  val label: String,
  val description: String,
  val pattern: List<Pair<BreathingPhase, Int>>
) {
  BOX(
    label = "Box Breathing",
    description = "4-4-4-4 pattern for calm and focus",
    pattern = listOf(
      BreathingPhase.INHALE to 4,
      BreathingPhase.HOLD to 4,
      BreathingPhase.EXHALE to 4,
      BreathingPhase.HOLD_AFTER_EXHALE to 4
    )
  ),
  FOUR_SEVEN_EIGHT(
    label = "4-7-8 Breathing",
    description = "Relaxation technique for sleep",
    pattern = listOf(
      BreathingPhase.INHALE to 4,
      BreathingPhase.HOLD to 7,
      BreathingPhase.EXHALE to 8
    )
  )
}

/**
 * Breathing phases during an exercise.
 */
enum class BreathingPhase(val label: String) {
  INHALE("Inhale"),
  HOLD("Hold"),
  EXHALE("Exhale"),
  HOLD_AFTER_EXHALE("Hold")
}


/**
 * Breathing Exercises screen with animated guides and timers.
 * No LLM processing required - purely static UI with animations.
 *
 * @param onNavigateBack Callback to navigate back to the dashboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExercisesScreen(
  onNavigateBack: () -> Unit
) {
  var selectedExercise by remember { mutableStateOf(BreathingExercise.BOX) }
  var isActive by remember { mutableStateOf(false) }
  var currentPhaseIndex by remember { mutableIntStateOf(0) }
  var phaseTimeRemaining by remember { mutableIntStateOf(0) }
  
  // Get current phase and duration
  val currentPattern = selectedExercise.pattern
  val (currentPhase, phaseDuration) = if (currentPattern.isNotEmpty()) {
    currentPattern[currentPhaseIndex % currentPattern.size]
  } else {
    BreathingPhase.INHALE to 4
  }
  
  // Timer logic
  LaunchedEffect(isActive, currentPhaseIndex) {
    if (isActive) {
      phaseTimeRemaining = phaseDuration
      while (phaseTimeRemaining > 0 && isActive) {
        delay(1000)
        phaseTimeRemaining--
      }
      if (isActive && phaseTimeRemaining == 0) {
        currentPhaseIndex++
      }
    }
  }
  
  // Animation for breathing circle
  val targetScale = when (currentPhase) {
    BreathingPhase.INHALE -> 1.5f
    BreathingPhase.EXHALE -> 0.5f
    else -> 1f
  }
  
  val animatedScale by animateFloatAsState(
    targetValue = if (isActive) targetScale else 1f,
    animationSpec = tween(
      durationMillis = if (isActive) phaseDuration * 1000 else 500,
      easing = LinearEasing
    ),
    label = "breathingScale"
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Breathing Exercises") },
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
        text = "Guided Breathing",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      
      Text(
        text = "Follow the breathing guide to calm your mind and body",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 24.dp)
      )
      
      // Exercise selection
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        BreathingExercise.entries.forEach { exercise ->
          FilterChip(
            selected = selectedExercise == exercise,
            onClick = {
              if (!isActive) {
                selectedExercise = exercise
                currentPhaseIndex = 0
                phaseTimeRemaining = 0
              }
            },
            label = { Text(exercise.label) },
            modifier = Modifier.weight(1f),
            enabled = !isActive
          )
        }
      }
      
      Spacer(modifier = Modifier.height(8.dp))
      
      // Exercise description
      Text(
        text = selectedExercise.description,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 24.dp)
      )
      
      // Animated breathing circle
      Box(
        modifier = Modifier
          .size(250.dp)
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(
          modifier = Modifier.fillMaxSize()
        ) {
          val radius = (size.minDimension / 2) * animatedScale
          drawCircle(
            color = when (currentPhase) {
              BreathingPhase.INHALE -> Color(0xFF4CAF50)
              BreathingPhase.EXHALE -> Color(0xFF2196F3)
              else -> Color(0xFFFF9800)
            },
            radius = radius,
            center = Offset(size.width / 2, size.height / 2),
            alpha = 0.6f
          )
        }
      }

      
      // Phase label and timer
      if (isActive) {
        Text(
          text = currentPhase.label,
          style = MaterialTheme.typography.headlineMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
          text = "$phaseTimeRemaining",
          style = MaterialTheme.typography.displayLarge,
          color = MaterialTheme.colorScheme.onSurface
        )
      } else {
        Text(
          text = "Ready to begin",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
      
      Spacer(modifier = Modifier.height(32.dp))
      
      // Control buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Start/Stop button
        Button(
          onClick = { isActive = !isActive },
          modifier = Modifier.weight(1f)
        ) {
          Text(if (isActive) "Stop" else "Start")
        }
        
        // Reset button
        OutlinedButton(
          onClick = {
            isActive = false
            currentPhaseIndex = 0
            phaseTimeRemaining = 0
          },
          modifier = Modifier.weight(1f)
        ) {
          Text("Reset")
        }
      }
      
      Spacer(modifier = Modifier.height(24.dp))
      
      // Pattern information
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      ) {
        Column(
          modifier = Modifier.padding(16.dp)
        ) {
          Text(
            text = "Pattern",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
          )
          
          selectedExercise.pattern.forEach { (phase, duration) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = phase.label,
                style = MaterialTheme.typography.bodyMedium
              )
              Text(
                text = "${duration}s",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    }
  }
}
