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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ai.edge.gallery.customtasks.mentalhealth.prompts.MentalHealthPrompts
import com.google.ai.edge.gallery.data.BuiltInTaskId
import com.google.ai.edge.gallery.data.ModelDownloadStatusType
import com.google.ai.edge.gallery.ui.llmsingleturn.LlmSingleTurnViewModel
import com.google.ai.edge.gallery.ui.modelmanager.ModelInitializationStatusType
import com.google.ai.edge.gallery.ui.modelmanager.ModelManagerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AGJournalToolsScreen"

/**
 * Journal action types for different AI-assisted operations.
 */
enum class JournalAction {
  SUMMARY,
  SUGGESTION
}

/**
 * Journal Tools screen that provides AI-assisted journaling with
 * summary and suggestion capabilities.
 *
 * @param modelManagerViewModel ViewModel for managing model state and lifecycle
 * @param onNavigateBack Callback to navigate back to the dashboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalToolsScreen(
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
  var journalEntry by remember { mutableStateOf("") }
  var response by remember { mutableStateOf("") }
  var actionType by remember { mutableStateOf<JournalAction?>(null) }
  var isGenerating by remember { mutableStateOf(false) }
  
  // Get the first available model
  val selectedModel = remember(task) {
    task?.models?.firstOrNull() ?: modelManagerUiState.selectedModel
  }
  
  // Initialize model when download completes
  val curDownloadStatus = modelManagerUiState.modelDownloadStatus[selectedModel.name]
  LaunchedEffect(curDownloadStatus, selectedModel.name) {
    if (curDownloadStatus?.status == ModelDownloadStatusType.SUCCEEDED && task != null) {
      Log.d(TAG, "Initializing model '${selectedModel.name}' for journal tools")
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
        title = { Text("Journal Tools") },
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
      // Title and description
      Text(
        text = "AI-Assisted Journaling",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      
      Text(
        text = "Write your thoughts and feelings, then choose to get a summary or gentle suggestions.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 24.dp)
      )
      
      // Journal entry input field
      OutlinedTextField(
        value = journalEntry,
        onValueChange = { journalEntry = it },
        label = { Text("Your journal entry") },
        placeholder = { Text("Write about your day, feelings, or thoughts...") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !isGenerating,
        minLines = 6,
        maxLines = 12
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // Action buttons row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Summarize button
        Button(
          onClick = {
            if (journalEntry.isNotBlank() && task != null && isModelInitialized) {
              response = ""
              actionType = JournalAction.SUMMARY
              val prompt = MentalHealthPrompts.SYSTEM_PROMPT + "\n\n" +
                MentalHealthPrompts.getJournalSummaryPrompt(journalEntry)
              
              scope.launch(Dispatchers.Default) {
                viewModel.generateResponse(
                  task = task,
                  model = selectedModel,
                  input = prompt
                )
              }
            }
          },
          enabled = journalEntry.isNotBlank() && !isGenerating && isModelInitialized,
          modifier = Modifier.weight(1f)
        ) {
          Text("Summarize")
        }

        
        // Get Suggestion button
        Button(
          onClick = {
            if (journalEntry.isNotBlank() && task != null && isModelInitialized) {
              response = ""
              actionType = JournalAction.SUGGESTION
              val prompt = MentalHealthPrompts.SYSTEM_PROMPT + "\n\n" +
                MentalHealthPrompts.getJournalSuggestionPrompt(journalEntry)
              
              scope.launch(Dispatchers.Default) {
                viewModel.generateResponse(
                  task = task,
                  model = selectedModel,
                  input = prompt
                )
              }
            }
          },
          enabled = journalEntry.isNotBlank() && !isGenerating && isModelInitialized,
          modifier = Modifier.weight(1f)
        ) {
          Text("Get Suggestion")
        }
      }
      
      // Model status messages
      if (!isModelInitialized && curDownloadStatus?.status == ModelDownloadStatusType.SUCCEEDED) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Initializing model...",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.secondary
        )
      }
      
      if (curDownloadStatus?.status == ModelDownloadStatusType.NOT_DOWNLOADED) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "Please download a model first",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.error
        )
      }
      
      // Loading indicator
      if (isGenerating) {
        Spacer(modifier = Modifier.height(24.dp))
        CircularProgressIndicator()
        Text(
          text = when (actionType) {
            JournalAction.SUMMARY -> "Generating summary..."
            JournalAction.SUGGESTION -> "Generating suggestions..."
            null -> "Processing..."
          },
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.padding(top = 8.dp)
        )
      }

      
      // Response display
      if (response.isNotEmpty() && !isGenerating && actionType != null) {
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
              text = when (actionType) {
                JournalAction.SUMMARY -> "Summary"
                JournalAction.SUGGESTION -> "Suggestions"
                null -> "Response"
              },
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
              text = response,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSecondaryContainer
            )
          }
        }
      }
    }
  }
}
