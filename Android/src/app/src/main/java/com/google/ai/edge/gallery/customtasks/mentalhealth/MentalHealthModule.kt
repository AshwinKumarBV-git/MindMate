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

import android.util.Log
import com.google.ai.edge.gallery.customtasks.common.CustomTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

private const val TAG = "MentalHealthModule"

/**
 * Hilt module that provides the Mental Health Companion CustomTask.
 *
 * This module registers the MentalHealthTask into the app's CustomTask set,
 * making it automatically discoverable on the home screen.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object MentalHealthModule {
  @Provides
  @IntoSet
  fun provideMentalHealthTask(): CustomTask {
    Log.d(TAG, "provideMentalHealthTask called - creating MentalHealthTask instance")
    try {
      val task = MentalHealthTask()
      Log.d(TAG, "MentalHealthTask instance created successfully")
      Log.d(TAG, "Task ID: ${task.task.id}")
      Log.d(TAG, "Task label: ${task.task.label}")
      Log.d(TAG, "Task icon: ${task.task.icon}")
      return task
    } catch (e: Exception) {
      Log.e(TAG, "ERROR creating MentalHealthTask", e)
      Log.e(TAG, "Exception type: ${e.javaClass.name}")
      Log.e(TAG, "Exception message: ${e.message}")
      e.printStackTrace()
      throw e
    }
  }
}
