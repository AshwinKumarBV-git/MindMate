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

package com.google.ai.edge.gallery.customtasks.mentalhealth

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import com.google.ai.edge.gallery.customtasks.common.CustomTask
import com.google.ai.edge.gallery.customtasks.common.CustomTaskData
import com.google.ai.edge.gallery.customtasks.mentalhealth.screens.MentalHealthHomeScreen
import com.google.ai.edge.gallery.data.CategoryInfo
import com.google.ai.edge.gallery.data.Model
import com.google.ai.edge.gallery.data.Task
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

private const val TAG = "MentalHealthTask"

/**
 * Mental Health Companion CustomTask implementation.
 *
 * Provides offline, privacy-focused mental health support tools including:
 * - Daily mood check-ins with AI-generated reflections
 * - CBT-based thought reframing
 * - AI-assisted journaling
 * - Safe companion chat with crisis detection
 * - Breathing exercises
 * - Grounding techniques
 * - Crisis support resources
 */
class MentalHealthTask @Inject constructor() : CustomTask {
  
  init {
    Log.d(TAG, "MentalHealthTask constructor called")
    try {
      Log.d(TAG, "Attempting to access Icons.Outlined.FavoriteBorder")
      val iconVector = Icons.Outlined.FavoriteBorder
      Log.d(TAG, "Icon vector obtained successfully: $iconVector")
      Log.d(TAG, "Icon name: ${iconVector.name}")
      Log.d(TAG, "Icon default width: ${iconVector.defaultWidth}")
      Log.d(TAG, "Icon default height: ${iconVector.defaultHeight}")
    } catch (e: Exception) {
      Log.e(TAG, "ERROR accessing icon in constructor", e)
      Log.e(TAG, "Exception type: ${e.javaClass.name}")
      Log.e(TAG, "Exception message: ${e.message}")
      e.printStackTrace()
    }
  }
  
  override val task: Task =
    Task(
      id = "mental_health_companion",
      label = "Mental Health Companion",
      category = CategoryInfo(id = "health", label = "Health & Wellness"),
      icon = Icons.Outlined.FavoriteBorder,
      description =
        "Offline mental health support with mood tracking, CBT techniques, journaling, " +
          "breathing exercises, grounding techniques, and crisis resources. All processing " +
          "happens on-device for maximum privacy.",
      docUrl = "",
      sourceCodeUrl = "",
      models = mutableListOf(),
      // Specify which models from the allowlist this task can use
      modelNames = listOf(
        "Gemma-3n-E2B-it",
        "Gemma-3n-E4B-it",
        "Gemma3-1B-IT"
      )
    ).also {
      Log.d(TAG, "Task object created successfully")
      Log.d(TAG, "Task ID: ${it.id}")
      Log.d(TAG, "Task label: ${it.label}")
      Log.d(TAG, "Task icon: ${it.icon}")
      Log.d(TAG, "Task category: ${it.category}")
      Log.d(TAG, "Task modelNames: ${it.modelNames}")
      Log.d(TAG, "Task modelNames size: ${it.modelNames.size}")
      Log.d(TAG, "Task models size: ${it.models.size}")
      Log.d(TAG, "Model names: ${it.modelNames}")
    }

  override fun initializeModelFn(
    context: Context,
    coroutineScope: CoroutineScope,
    model: Model,
    onDone: (String) -> Unit,
  ) {
    Log.d(TAG, "initializeModelFn called for model: ${model.name}")
    try {
      // Delegate to existing ModelManager infrastructure
      // The app's ModelManager will handle model initialization
      Log.d(TAG, "Model initialization delegated to ModelManager")
      onDone("")
      Log.d(TAG, "initializeModelFn completed successfully")
    } catch (e: Exception) {
      Log.e(TAG, "ERROR in initializeModelFn", e)
      Log.e(TAG, "Exception type: ${e.javaClass.name}")
      Log.e(TAG, "Exception message: ${e.message}")
      e.printStackTrace()
      onDone(e.message ?: "Unknown error in initializeModelFn")
    }
  }

  override fun cleanUpModelFn(
    context: Context,
    coroutineScope: CoroutineScope,
    model: Model,
    onDone: () -> Unit,
  ) {
    Log.d(TAG, "cleanUpModelFn called for model: ${model.name}")
    try {
      // Delegate to existing ModelManager infrastructure
      // The app's ModelManager will handle model cleanup
      Log.d(TAG, "Model cleanup delegated to ModelManager")
      onDone()
      Log.d(TAG, "cleanUpModelFn completed successfully")
    } catch (e: Exception) {
      Log.e(TAG, "ERROR in cleanUpModelFn", e)
      Log.e(TAG, "Exception type: ${e.javaClass.name}")
      Log.e(TAG, "Exception message: ${e.message}")
      e.printStackTrace()
      onDone()
    }
  }

  @Composable
  override fun MainScreen(data: Any) {
    Log.d(TAG, "MainScreen composable called")
    Log.d(TAG, "Casting data to CustomTaskData")
    val customTaskData = data as CustomTaskData
    Log.d(TAG, "CustomTaskData cast successful")
    Log.d(TAG, "Bottom padding: ${customTaskData.bottomPadding}")
    
    Log.d(TAG, "Calling MentalHealthHomeScreen")
    MentalHealthHomeScreen(
      modelManagerViewModel = customTaskData.modelManagerViewModel,
      bottomPadding = customTaskData.bottomPadding
    )
    Log.d(TAG, "MentalHealthHomeScreen rendered successfully")
  }
}
