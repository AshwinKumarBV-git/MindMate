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

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.ai.edge.gallery.ui.common.GlassCard
import com.google.ai.edge.gallery.ui.common.GradientBackground
import com.google.ai.edge.gallery.ui.modelmanager.ModelManagerViewModel

private const val TAG = "MentalHealthHomeScreen"

/**
 * Navigation destinations for Mental Health Companion features.
 */
sealed class MentalHealthScreen {
  object Dashboard : MentalHealthScreen()
  object MoodCheckIn : MentalHealthScreen()
  object ThoughtReframing : MentalHealthScreen()
  object JournalTools : MentalHealthScreen()
  object CompanionChat : MentalHealthScreen()
  object BreathingExercises : MentalHealthScreen()
  object GroundingExercises : MentalHealthScreen()
  object CrisisSupport : MentalHealthScreen()
}

/**
 * Main dashboard screen for the Mental Health Companion.
 *
 * This screen displays navigation options to all mental health features.
 */
@Composable
fun MentalHealthHomeScreen(
  modelManagerViewModel: ModelManagerViewModel,
  bottomPadding: Dp
) {
  Log.d(TAG, "MentalHealthHomeScreen composable started")
  Log.d(TAG, "Bottom padding: $bottomPadding")
  
  var currentScreen by remember { 
    Log.d(TAG, "Initializing currentScreen state to Dashboard")
    mutableStateOf<MentalHealthScreen>(MentalHealthScreen.Dashboard) 
  }
  
  Log.d(TAG, "Current screen: $currentScreen")
  
  when (currentScreen) {
    MentalHealthScreen.Dashboard -> {
      Log.d(TAG, "Rendering Dashboard")
      GradientBackground {
        DashboardContent(
          bottomPadding = bottomPadding,
          onNavigate = { screen -> 
            Log.d(TAG, "Navigation requested to: $screen")
            currentScreen = screen 
          }
        )
      }
    }
    MentalHealthScreen.MoodCheckIn -> MoodCheckInScreen(
      modelManagerViewModel = modelManagerViewModel,
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard }
    )
    MentalHealthScreen.ThoughtReframing -> ThoughtReframingScreen(
      modelManagerViewModel = modelManagerViewModel,
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard }
    )
    MentalHealthScreen.JournalTools -> JournalToolsScreen(
      modelManagerViewModel = modelManagerViewModel,
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard }
    )
    MentalHealthScreen.CompanionChat -> SafeCompanionChatScreen(
      modelManagerViewModel = modelManagerViewModel,
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard },
      onNavigateToCrisis = { currentScreen = MentalHealthScreen.CrisisSupport }
    )
    MentalHealthScreen.BreathingExercises -> BreathingExercisesScreen(
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard }
    )
    MentalHealthScreen.GroundingExercises -> GroundingExercisesScreen(
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard }
    )
    MentalHealthScreen.CrisisSupport -> CrisisSupportScreen(
      onNavigateBack = { currentScreen = MentalHealthScreen.Dashboard }
    )
  }
}

@Composable
private fun DashboardContent(
  bottomPadding: Dp,
  onNavigate: (MentalHealthScreen) -> Unit
) {
  Log.d(TAG, "DashboardContent composable started")
  Log.d(TAG, "Dashboard bottom padding: $bottomPadding")
  
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = bottomPadding)
      .padding(16.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Mental Health Companion",
      style = MaterialTheme.typography.headlineMedium,
      textAlign = TextAlign.Center,
      color = Color(0xFF1A1A1A),
      modifier = Modifier.padding(vertical = 16.dp)
    )
    
    Text(
      text = "Choose a tool to support your well-being",
      style = MaterialTheme.typography.bodyMedium,
      textAlign = TextAlign.Center,
      color = Color(0xFF2A2A2A),
      modifier = Modifier.padding(bottom = 24.dp)
    )
    
    FeatureCard(
      title = "Daily Mood Check-In",
      description = "Track your mood and receive supportive reflections",
      onClick = { onNavigate(MentalHealthScreen.MoodCheckIn) }
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    FeatureCard(
      title = "Thought Reframing",
      description = "Reframe negative thoughts using CBT techniques",
      onClick = { onNavigate(MentalHealthScreen.ThoughtReframing) }
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    FeatureCard(
      title = "Journal Tools",
      description = "AI-assisted journaling with summaries and suggestions",
      onClick = { onNavigate(MentalHealthScreen.JournalTools) }
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    FeatureCard(
      title = "Companion Chat",
      description = "Have a supportive conversation with AI",
      onClick = { onNavigate(MentalHealthScreen.CompanionChat) }
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    FeatureCard(
      title = "Breathing Exercises",
      description = "Guided breathing to calm your nervous system",
      onClick = { onNavigate(MentalHealthScreen.BreathingExercises) }
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    FeatureCard(
      title = "Grounding Exercises",
      description = "5-4-3-2-1 technique to reconnect with the present",
      onClick = { onNavigate(MentalHealthScreen.GroundingExercises) }
    )
    
    Spacer(modifier = Modifier.height(12.dp))
    
    FeatureCard(
      title = "⚠️ Crisis Support",
      description = "Immediate access to crisis resources",
      onClick = { onNavigate(MentalHealthScreen.CrisisSupport) },
      isUrgent = true
    )
  }
}

@Composable
private fun FeatureCard(
  title: String,
  description: String,
  onClick: () -> Unit,
  isUrgent: Boolean = false
) {
  Log.d(TAG, "Rendering FeatureCard: $title")
  
  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
    backgroundColor = if (isUrgent) {
      MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
    } else {
      MaterialTheme.colorScheme.surface.copy(alpha = 0.20f)
    }
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = if (isUrgent) {
          Color(0xFFB00020)
        } else {
          Color(0xFF1A1A1A)
        }
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF3F3F3F)
      )
    }
  }
}
