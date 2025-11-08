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

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.ai.edge.gallery.customtasks.mentalhealth.prompts.MentalHealthPrompts
import com.google.ai.edge.gallery.customtasks.mentalhealth.utils.CrisisDetection
import com.google.ai.edge.gallery.data.BuiltInTaskId
import com.google.ai.edge.gallery.firebaseAnalytics
import com.google.ai.edge.gallery.ui.common.chat.ChatMessageAudioClip
import com.google.ai.edge.gallery.ui.common.chat.ChatMessageImage
import com.google.ai.edge.gallery.ui.common.chat.ChatMessageText
import com.google.ai.edge.gallery.ui.common.chat.ChatView
import com.google.ai.edge.gallery.ui.llmchat.LlmChatViewModel
import com.google.ai.edge.gallery.ui.modelmanager.ModelManagerViewModel

private const val TAG = "AGSafeCompanionChatScreen"

/**
 * Safe Companion Chat screen that wraps the existing LLMChatScreen with crisis detection.
 * 
 * This screen intercepts user messages before sending them to the LLM, applies crisis detection,
 * and redirects to crisis support resources if needed. Otherwise, it passes messages to the
 * LLM with the mental health system prompt injected.
 *
 * @param modelManagerViewModel ViewModel for managing model state and lifecycle
 * @param onNavigateBack Callback to navigate back to the dashboard
 * @param onNavigateToCrisis Callback to navigate to the Crisis Support screen
 */
@Composable
fun SafeCompanionChatScreen(
  modelManagerViewModel: ModelManagerViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToCrisis: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: LlmChatViewModel = hiltViewModel()
) {
  val context = LocalContext.current
  val task = modelManagerViewModel.getTaskById(id = BuiltInTaskId.LLM_CHAT)!!

  
  // Track if system prompt has been injected for the current session
  var systemPromptInjected by remember { mutableStateOf(false) }
  
  // Wrap ChatView with crisis detection and system prompt injection
  ChatView(
    task = task,
    viewModel = viewModel,
    modelManagerViewModel = modelManagerViewModel,
    onSendMessage = { model, messages ->
      // Extract text from messages for crisis detection
      var textContent = ""
      var chatMessageText: ChatMessageText? = null
      val images: MutableList<Bitmap> = mutableListOf()
      val audioMessages: MutableList<ChatMessageAudioClip> = mutableListOf()
      
      for (message in messages) {
        when (message) {
          is ChatMessageText -> {
            chatMessageText = message
            textContent = message.content
          }
          is ChatMessageImage -> {
            images.addAll(message.bitmaps)
          }
          is ChatMessageAudioClip -> {
            audioMessages.add(message)
          }
        }
      }
      
      // Apply crisis detection to text content
      if (textContent.isNotEmpty() && CrisisDetection.detectCrisis(textContent)) {
        Log.d(TAG, "Crisis keywords detected in user input. Navigating to crisis support.")
        onNavigateToCrisis()
        return@ChatView
      }
      
      // If no crisis detected, proceed with normal flow
      // Inject system prompt on first message
      val messagesToSend = if (!systemPromptInjected && chatMessageText != null) {
        systemPromptInjected = true
        Log.d(TAG, "Injecting system prompt into first message")
        
        // Create a new message with system prompt prepended
        val modifiedText = MentalHealthPrompts.SYSTEM_PROMPT + "\n\n" + textContent
        val modifiedMessage = ChatMessageText(
          content = modifiedText,
          side = chatMessageText.side
        )
        
        // Replace the original text message with the modified one
        messages.map { msg ->
          if (msg is ChatMessageText) modifiedMessage else msg
        }
      } else {
        messages
      }
      
      // Add messages to the chat
      for (message in messagesToSend) {
        viewModel.addMessage(model = model, message = message)
      }

      
      // Generate response if there's text or audio content
      if ((textContent.isNotEmpty() && chatMessageText != null) || audioMessages.isNotEmpty()) {
        modelManagerViewModel.addTextInputHistory(textContent)
        viewModel.generateResponse(
          model = model,
          input = if (systemPromptInjected && !textContent.isEmpty()) {
            // For subsequent messages, we don't prepend system prompt again
            // as it's already in the conversation history
            textContent
          } else {
            textContent
          },
          images = images,
          audioMessages = audioMessages,
          onError = {
            viewModel.handleError(
              context = context,
              task = task,
              model = model,
              modelManagerViewModel = modelManagerViewModel,
              triggeredMessage = chatMessageText,
            )
          },
        )
        
        firebaseAnalytics?.logEvent(
          "generate_action",
          bundleOf("capability_name" to task.id, "model_id" to model.name),
        )
      }
    },
    onRunAgainClicked = { model, message ->
      // Apply crisis detection before running again
      if (message is ChatMessageText) {
        if (CrisisDetection.detectCrisis(message.content)) {
          Log.d(TAG, "Crisis keywords detected in run again. Navigating to crisis support.")
          onNavigateToCrisis()
          return@ChatView
        }
        
        viewModel.runAgain(
          model = model,
          message = message,
          onError = {
            viewModel.handleError(
              context = context,
              task = task,
              model = model,
              modelManagerViewModel = modelManagerViewModel,
              triggeredMessage = message,
            )
          },
        )
      }
    },
    onBenchmarkClicked = { _, _, _, _ -> },
    onResetSessionClicked = { model ->
      // Reset the system prompt injection flag when session is reset
      systemPromptInjected = false
      viewModel.resetSession(task = task, model = model)
    },
    showStopButtonInInputWhenInProgress = true,
    onStopButtonClicked = { model -> viewModel.stopResponse(model = model) },
    navigateUp = onNavigateBack,
    modifier = modifier,
  )
}
