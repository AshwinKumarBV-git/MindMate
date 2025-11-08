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

package com.google.ai.edge.gallery.ui.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.ai.edge.gallery.R
import com.google.ai.edge.gallery.data.Task

private const val TAG = "TaskIcon"

private val SHAPES: List<Int> =
  listOf(
    // Ask image.
    R.drawable.circle,
    // Audio scribe
    R.drawable.double_circle,
    // Prompt lab
    R.drawable.pantegon,
    // AI chat,
    R.drawable.four_circle,
  )

/**
 * Composable that displays an icon representing a task. It consists of a background image and a
 * foreground icon, both centered within a square box.
 */
@Composable
fun TaskIcon(
  task: Task,
  modifier: Modifier = Modifier,
  width: Dp = 56.dp,
  animationProgress: Float = 1f,
) {
  Log.d(TAG, "========== TaskIcon START ==========")
  Log.d(TAG, "Task ID: ${task.id}")
  Log.d(TAG, "Task label: ${task.label}")
  Log.d(TAG, "Task icon (ImageVector): ${task.icon}")
  Log.d(TAG, "Task iconVectorResourceId: ${task.iconVectorResourceId}")
  Log.d(TAG, "Task index: ${task.index}")
  Log.d(TAG, "Width: $width, Animation progress: $animationProgress")
  
  val revealingBrush =
    linearGradient(
      colorStops =
        arrayOf(
          (1f + 0.2f) * (1 - animationProgress) - 0.2f to Color.Red,
          (1f + 0.2f) * (1 - animationProgress) to Color.Transparent,
        )
    )
  Log.d(TAG, "Revealing brush created successfully")
  
  Box(modifier = modifier.width(width).aspectRatio(1f), contentAlignment = Alignment.Center) {
    Log.d(TAG, "Creating background gradient brush")
    val brush = linearGradient(colors = getTaskBgGradientColors(task = task))
    Log.d(TAG, "Background brush created successfully")
    
    Log.d(TAG, "Creating background shape image")
    Image(
      painter = getTaskIconBgShape(task = task),
      contentDescription = null,
      modifier =
        Modifier.fillMaxSize()
          .graphicsLayer(
            // This is important to make blending mode work.
            alpha = 0.99f,
            compositingStrategy = CompositingStrategy.Offscreen,
            translationX = 80 * (1 - animationProgress),
            rotationZ = -180 * (1 - animationProgress),
          )
          .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcIn)
            drawRect(brush = revealingBrush, blendMode = BlendMode.DstOut)
          },
      contentScale = ContentScale.FillHeight,
    )
    Log.d(TAG, "Background image rendered successfully")
    
    var iconAnimationProgress = 0f
    if (animationProgress >= 0.8) {
      iconAnimationProgress = (animationProgress - 0.8f) / 0.2f
    }
    Log.d(TAG, "Icon animation progress: $iconAnimationProgress")
    
    Log.d(TAG, "Attempting to load icon...")
    Log.d(TAG, "task.icon is null: ${task.icon == null}")
    Log.d(TAG, "task.iconVectorResourceId: ${task.iconVectorResourceId}")
    
    val iconVector = if (task.icon != null) {
      Log.d(TAG, "Using task.icon (ImageVector)")
      Log.d(TAG, "Icon name: ${task.icon!!.name}")
      Log.d(TAG, "Icon default width: ${task.icon!!.defaultWidth}")
      Log.d(TAG, "Icon default height: ${task.icon!!.defaultHeight}")
      task.icon!!
    } else {
      Log.d(TAG, "task.icon is null, attempting to load from resource ID: ${task.iconVectorResourceId}")
      if (task.iconVectorResourceId == null) {
        Log.e(TAG, "ERROR: Both task.icon and task.iconVectorResourceId are null!")
        throw IllegalStateException("Task ${task.id} has no icon defined")
      }
      Log.d(TAG, "Loading ImageVector from resource ID: 0x${task.iconVectorResourceId!!.toString(16)}")
      val vector = ImageVector.vectorResource(task.iconVectorResourceId!!)
      Log.d(TAG, "ImageVector loaded successfully from resource")
      Log.d(TAG, "Loaded icon name: ${vector.name}")
      vector
    }
    
    Log.d(TAG, "Rendering Icon composable with vector: $iconVector")
    Icon(
      iconVector,
      tint = Color.White,
      modifier =
        Modifier.size(width * 0.55f)
          .graphicsLayer { alpha = iconAnimationProgress }
          .scale(iconAnimationProgress),
      contentDescription = null,
    )
    Log.d(TAG, "Icon rendered successfully")
    Log.d(TAG, "========== TaskIcon END ==========")
  }
}

@Composable
private fun getTaskIconBgShape(task: Task): Painter {
  Log.d(TAG, "getTaskIconBgShape called for task: ${task.id}")
  val colorIndex: Int = task.index % SHAPES.size
  Log.d(TAG, "Color index: $colorIndex (task.index=${task.index}, SHAPES.size=${SHAPES.size})")
  Log.d(TAG, "Selected shape resource ID: 0x${SHAPES[colorIndex].toString(16)}")
  
  val painter = painterResource(SHAPES[colorIndex])
  Log.d(TAG, "Painter loaded successfully")
  return painter
}

// @Preview(showBackground = true)
// @Composable
// fun TaskIconPreview() {
//   for ((index, task) in TASKS.withIndex()) {
//     task.index = index
//   }
//
//   GalleryTheme {
//     Column(modifier = Modifier.background(Color.Gray)) {
//       TaskIcon(task = TASK_LLM_CHAT, width = 80.dp)
//     }
//   }
// }
