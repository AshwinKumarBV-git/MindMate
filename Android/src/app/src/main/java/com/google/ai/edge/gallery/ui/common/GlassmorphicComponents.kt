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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * A glassmorphic card component with translucent background, subtle border, and rounded corners.
 * Perfect for creating modern, calm UI elements with depth and layering.
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape of the card, defaults to 24dp rounded corners
 * @param backgroundColor Translucent background color, defaults to 20% white
 * @param borderColor Border color, defaults to 25% white
 * @param borderWidth Width of the border, defaults to 1dp
 * @param elevation Elevation of the card, defaults to 0dp for flat glassmorphic look
 * @param content Content to be displayed inside the card
 */
@Composable
fun GlassCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(24.dp),
  backgroundColor: Color = Color.White.copy(alpha = 0.20f),
  borderColor: Color = Color.White.copy(alpha = 0.25f),
  borderWidth: Dp = 1.dp,
  elevation: Dp = 0.dp,
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    modifier = modifier,
    shape = shape,
    colors = CardDefaults.cardColors(
      containerColor = backgroundColor
    ),
    border = BorderStroke(borderWidth, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = elevation)
  ) {
    Column(content = content)
  }
}

/**
 * A larger glassmorphic surface component for screens and major sections.
 * Similar to GlassCard but uses Surface and Box for more flexible layouts.
 *
 * @param modifier Modifier to be applied to the surface
 * @param shape Shape of the surface, defaults to 24dp rounded corners
 * @param backgroundColor Translucent background color, defaults to 15% white
 * @param borderColor Border color, defaults to 20% white
 * @param borderWidth Width of the border, defaults to 1dp
 * @param content Content to be displayed inside the surface
 */
@Composable
fun GlassSurface(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(24.dp),
  backgroundColor: Color = Color.White.copy(alpha = 0.15f),
  borderColor: Color = Color.White.copy(alpha = 0.20f),
  borderWidth: Dp = 1.dp,
  content: @Composable BoxScope.() -> Unit
) {
  Surface(
    modifier = modifier,
    shape = shape,
    color = backgroundColor,
    border = BorderStroke(borderWidth, borderColor),
    tonalElevation = 0.dp
  ) {
    Box(content = content)
  }
}

/**
 * A gradient background component that creates a soft, calming radial gradient.
 * Perfect for screen backgrounds in the MindMate mental wellness theme.
 *
 * @param colors List of colors for the gradient, defaults to lavender → blue → teal
 * @param center Center point of the radial gradient as a fraction (0.0 to 1.0)
 * @param radius Radius of the gradient effect
 * @param modifier Modifier to be applied to the background
 * @param content Content to be displayed on top of the gradient
 */
@Composable
fun GradientBackground(
  colors: List<Color> = listOf(
    Color(0xFFC9B7E8),  // Lavender haze
    Color(0xFFA7C7E7),  // Soft blue
    Color(0xFF9AD1C6)   // Teal mist
  ),
  center: Offset = Offset(0.5f, 0.3f),
  radius: Float = 1200f,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        brush = Brush.radialGradient(
          colors = colors,
          center = center,
          radius = radius
        )
      )
  ) {
    content()
  }
}

/**
 * A linear gradient background component for more directional gradient effects.
 * Useful for headers, footers, or sections that need a directional flow.
 *
 * @param colors List of colors for the gradient
 * @param startOffset Starting point of the gradient (0.0 to 1.0)
 * @param endOffset Ending point of the gradient (0.0 to 1.0)
 * @param modifier Modifier to be applied to the background
 * @param content Content to be displayed on top of the gradient
 */
@Composable
fun LinearGradientBackground(
  colors: List<Color> = listOf(
    Color(0xFFC9B7E8),  // Lavender haze
    Color(0xFFA7C7E7)   // Soft blue
  ),
  startOffset: Offset = Offset(0f, 0f),
  endOffset: Offset = Offset(0f, Float.POSITIVE_INFINITY),
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        brush = Brush.linearGradient(
          colors = colors,
          start = startOffset,
          end = endOffset
        )
      )
  ) {
    content()
  }
}

/**
 * A glassmorphic button-style surface with slightly higher opacity for interactive elements.
 * Perfect for buttons, chips, and other clickable components.
 *
 * @param modifier Modifier to be applied to the surface
 * @param shape Shape of the surface, defaults to 24dp rounded corners
 * @param backgroundColor Translucent background color, defaults to 25% white
 * @param borderColor Border color, defaults to 30% white
 * @param borderWidth Width of the border, defaults to 1dp
 * @param content Content to be displayed inside the surface
 */
@Composable
fun GlassButton(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(24.dp),
  backgroundColor: Color = Color.White.copy(alpha = 0.25f),
  borderColor: Color = Color.White.copy(alpha = 0.30f),
  borderWidth: Dp = 1.dp,
  content: @Composable BoxScope.() -> Unit
) {
  Surface(
    modifier = modifier,
    shape = shape,
    color = backgroundColor,
    border = BorderStroke(borderWidth, borderColor),
    tonalElevation = 0.dp
  ) {
    Box(content = content)
  }
}
