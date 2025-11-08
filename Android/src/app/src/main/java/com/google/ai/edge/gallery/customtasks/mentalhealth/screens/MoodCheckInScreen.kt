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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ai.edge.gallery.customtasks.mentalhealth.prompts.MentalHealthPrompts
import com.google.ai.edge.gallery.data.BuiltInTaskId
import com.google.ai.edge.gallery.data.ModelDownloadStatusType
import com.google.ai.edge.gallery.ui.common.GradientBackground
import com.google.ai.edge.gallery.ui.llmsingleturn.LlmSingleTurnViewModel
import com.google.ai.edge.gallery.ui.modelmanager.ModelInitializationStatusType
import com.google.ai.edge.gallery.ui.modelmanager.ModelManagerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AGMoodCheckInScreen"

/**
 * Mood enum representing different emotional states.
 */
enum class Mood(val label: String, val emoji: String) {
  GREAT("Great", "😊"),
  GOOD("Good", "🙂"),
  OKAY("Okay", "😐"),
  STRUGGLING("Struggling", "😔"),
  DIFFICULT("Difficult", "😢")
}

/**
 * Mood Check-In screen that allows users to select their current mood
 * and receive AI-generated supportive reflections.
 *
 * @param modelManagerViewModel ViewModel for managing model state and lifecycle
 * @param onNavigateBack Callback to navigate back to the dashboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodCheckInScreen(
  modelManagerViewModel: ModelManagerViewModel,
  onNavigateBack: () -> Unit,
  viewModel: LlmSingleTurnViewModel = hiltViewModel()
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  
  // Get the LLM task for model access
  val task = modelManagerViewModel.getTaskById(id = BuiltInTaskId.LLM_PROMPT_LAB)
  
  // State management
  val modelManagerUiState by modelManagerViewModel.uiState.collectAsState()
  val llmUiState by viewModel.uiState.collectAsState()
  var selectedMood by remember { mutableStateOf<Mood?>(null) }
  var contextText by remember { mutableStateOf("") }
  var response by remember { mutableStateOf("") }
  var isGenerating by remember { mutableStateOf(false) }
  
  // Get the first available model
  val selectedModel = remember(task) {
    task?.models?.firstOrNull() ?: modelManagerUiState.selectedModel
  }
  
  // Initialize model when download completes
  val curDownloadStatus = modelManagerUiState.modelDownloadStatus[selectedModel.name]
  LaunchedEffect(curDownloadStatus, selectedModel.name) {
    if (curDownloadStatus?.status == ModelDownloadStatusType.SUCCEEDED && task != null) {
      Log.d(TAG, "Initializing model '${selectedModel.name}' for mood check-in")
      modelManagerViewModel.initializeModel(context, task = task, model = selectedModel)
    }
  }
  
  // Track model initialization status
  val modelInitializationStatus = modelManagerUiState.modelInitializationStatus[selectedModel.name]
  val isModelInitialized = modelInitializationStatus?.status == ModelInitializationStatusType.INITIALIZED
  
  // Update response from LLM
  LaunchedEffect(llmUiState.responsesByModel, selectedModel.name) {
    val modelResponses = llmUiState.responsesByModel[selectedModel.name]
    if (modelResponses != null && modelResponses.isNotEmpty()) {
      response = modelResponses.values.firstOrNull() ?: ""
    }
  }
  
  // Track generation progress
  LaunchedEffect(llmUiState.inProgress) {
    isGenerating = llmUiState.inProgress || llmUiState.preparing
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Daily Mood Check-In") },
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
    GradientBackground {
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
        text = "How are you feeling today?",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        color = Color(0xFF1A1A1A),
        modifier = Modifier.padding(bottom = 24.dp)
      )
      
      // Mood selection buttons
      Mood.entries.forEach { mood ->
        MoodButton(
          mood = mood,
          isSelected = selectedMood == mood,
          onClick = { selectedMood = mood },
          enabled = !isGenerating
        )
        Spacer(modifier = Modifier.height(12.dp))
      }
      
      Spacer(modifier = Modifier.height(24.dp))
      
      // Optional context text input
      OutlinedTextField(
        value = contextText,
        onValueChange = { contextText = it },
        label = { Text("Add context (optional)") },
        placeholder = { Text("What's on your mind?") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !isGenerating,
        minLines = 3,
        maxLines = 5
      )
      
      Spacer(modifier = Modifier.height(16.dp))

      // Generate button
      Button(
        onClick = {
          if (selectedMood != null && task != null && isModelInitialized) {
            response = ""
            val prompt = MentalHealthPrompts.SYSTEM_PROMPT + "\n\n" +
              MentalHealthPrompts.getMoodCheckInPrompt(selectedMood!!.label, contextText)
            
            scope.launch(Dispatchers.Default) {
              viewModel.generateResponse(
                task = task,
                model = selectedModel,
                input = prompt
              )
            }
          }
        },
        enabled = selectedMood != null && !isGenerating && isModelInitialized,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(if (isGenerating) "Generating..." else "Get Reflection")
      }
      
      // Model status messages
      if (!isModelInitialized && curDownloadStatus?.status == ModelDownloadStatusType.SUCCEEDED) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Initializing model...",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF2A2A2A)
        )
      }
      
      if (curDownloadStatus?.status == ModelDownloadStatusType.NOT_DOWNLOADED) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Please download a model first",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFFB00020)
        )
      }
      
      // Loading indicator
      if (isGenerating) {
        Spacer(modifier = Modifier.height(24.dp))
        CircularProgressIndicator()
      }
      
      // Response display
      if (response.isNotEmpty() && !isGenerating) {
        Spacer(modifier = Modifier.height(24.dp))
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
          )
        ) {
          Column(
            modifier = Modifier.padding(16.dp)
          ) {
            Text(
              text = "Reflection",
              style = MaterialTheme.typography.titleMedium,
              color = Color(0xFF1A1A1A),
              modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
              text = response,
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF2A2A2A)
            )
          }
        }
      }
    }
    }
  }
}


/**
 * Mood selection button component.
 *
 * @param mood The mood option to display
 * @param isSelected Whether this mood is currently selected
 * @param onClick Callback when the button is clicked
 * @param enabled Whether the button is enabled
 */
@Composable
private fun MoodButton(
  mood: Mood,
  isSelected: Boolean,
  onClick: () -> Unit,
  enabled: Boolean
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp),
    colors = if (isSelected) {
      ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
      )
    } else {
      ButtonDefaults.outlinedButtonColors()
    }
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = mood.emoji,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(end = 16.dp)
      )
      Text(
        text = mood.label,
        style = MaterialTheme.typography.titleMedium
      )
    }
  }
}
