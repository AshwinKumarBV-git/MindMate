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

package com.google.ai.edge.gallery.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.ai.edge.gallery.proto.Theme

private val lightScheme =
  lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
  )

private val darkScheme =
  darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
  )

@Immutable
data class CustomColors(
  val appTitleGradientColors: List<Color> = listOf(),
  val tabHeaderBgColor: Color = Color.Transparent,
  val taskCardBgColor: Color = Color.Transparent,
  val taskBgColors: List<Color> = listOf(),
  val taskBgGradientColors: List<List<Color>> = listOf(),
  val taskIconColors: List<Color> = listOf(),
  val taskIconShapeBgColor: Color = Color.Transparent,
  val homeBottomGradient: List<Color> = listOf(),
  val userBubbleBgColor: Color = Color.Transparent,
  val agentBubbleBgColor: Color = Color.Transparent,
  val linkColor: Color = Color.Transparent,
  val successColor: Color = Color.Transparent,
  val recordButtonBgColor: Color = Color.Transparent,
  val waveFormBgColor: Color = Color.Transparent,
  val modelInfoIconColor: Color = Color.Transparent,
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

val lightCustomColors =
  CustomColors(
    appTitleGradientColors = listOf(Color(0xFFA7C7E7), Color(0xFF9AD1C6)),  // Soft blue to teal
    tabHeaderBgColor = Color(0xFFA7C7E7),  // Soft blue
    taskCardBgColor = Color(0x40FFFFFF),  // Translucent white (glassmorphism)
    taskBgColors =
      listOf(
        // Soft lavender
        Color(0xFFF5E8F8),
        // Soft teal
        Color(0xFFE8F5F2),
        // Soft blue
        Color(0xFFE8F0F7),
        // Soft cream
        Color(0xFFF5F0E8),
      ),
    taskBgGradientColors =
      listOf(
        // Lavender gradient
        listOf(Color(0xFFC9B7E8), Color(0xFFB7A0D8)),
        // Teal gradient
        listOf(Color(0xFF9AD1C6), Color(0xFF7AB9AE)),
        // Blue gradient
        listOf(Color(0xFFA7C7E7), Color(0xFF87A7C7)),
        // Cream gradient
        listOf(Color(0xFFE8D4B0), Color(0xFFC8B490)),
      ),
    taskIconColors =
      listOf(
        // Lavender
        Color(0xFFB7A0D8),
        // Teal
        Color(0xFF7AB9AE),
        // Blue
        Color(0xFF87A7C7),
        // Cream
        Color(0xFFC8B490),
      ),
    taskIconShapeBgColor = Color(0xFFFAFBFC),  // Off-white
    homeBottomGradient = listOf(Color(0x00F5F7FA), Color(0xFFE8F0F7)),  // Fade to soft blue
    agentBubbleBgColor = Color(0x40A7C7E7),  // Translucent blue (glassmorphism)
    userBubbleBgColor = Color(0xFF9AD1C6),  // Teal
    linkColor = Color(0xFF7A9FBF),  // Muted blue
    successColor = Color(0xFF7AB9AE),  // Teal
    recordButtonBgColor = Color(0xFFC9B7E8),  // Lavender
    waveFormBgColor = Color(0xFFB0C0D0),  // Soft gray-blue
    modelInfoIconColor = Color(0xFFD0DCE8),  // Very soft outline
  )

val darkCustomColors =
  CustomColors(
    appTitleGradientColors = listOf(Color(0xFF7A9FBF), Color(0xFF6FA99E)),  // Muted blue to teal
    tabHeaderBgColor = Color(0xFF7A9FBF),  // Muted blue
    taskCardBgColor = Color(0x30FFFFFF),  // Translucent white (darker mode glassmorphism)
    taskBgColors =
      listOf(
        // Dark lavender
        Color(0xFF2A2238),
        // Dark teal
        Color(0xFF223A35),
        // Dark blue
        Color(0xFF223848),
        // Dark cream
        Color(0xFF3A3228),
      ),
    taskBgGradientColors =
      listOf(
        // Muted lavender gradient
        listOf(Color(0xFF9F8FC0), Color(0xFF8F7FB0)),
        // Muted teal gradient
        listOf(Color(0xFF6FA99E), Color(0xFF5F998E)),
        // Muted blue gradient
        listOf(Color(0xFF7A9FBF), Color(0xFF6A8FAF)),
        // Muted cream gradient
        listOf(Color(0xFFC8B490), Color(0xFFB8A480)),
      ),
    taskIconColors =
      listOf(
        // Muted lavender
        Color(0xFF9F8FC0),
        // Muted teal
        Color(0xFF6FA99E),
        // Muted blue
        Color(0xFF7A9FBF),
        // Muted cream
        Color(0xFFC8B490),
      ),
    taskIconShapeBgColor = Color(0xFF1E2E3E),  // Dark surface
    homeBottomGradient = listOf(Color(0x001A2A3A), Color(0x30223848)),  // Fade to dark blue
    agentBubbleBgColor = Color(0x307A9FBF),  // Translucent muted blue (glassmorphism)
    userBubbleBgColor = Color(0xFF6FA99E),  // Muted teal
    linkColor = Color(0xFFA7C7E7),  // Soft blue
    successColor = Color(0xFF9AD1C6),  // Teal
    recordButtonBgColor = Color(0xFF9F8FC0),  // Muted lavender
    waveFormBgColor = Color(0xFF4A5A6A),  // Muted gray
    modelInfoIconColor = Color(0xFF3A4A5A),  // Subtle outline
  )

val MaterialTheme.customColors: CustomColors
  @Composable @ReadOnlyComposable get() = LocalCustomColors.current

/**
 * Controls the color of the phone's status bar icons based on whether the app is using a dark
 * theme.
 */
@Composable
fun StatusBarColorController(useDarkTheme: Boolean) {
  val view = LocalView.current
  val currentWindow = (view.context as? Activity)?.window

  if (currentWindow != null) {
    SideEffect {
      WindowCompat.setDecorFitsSystemWindows(currentWindow, false)
      val controller = WindowCompat.getInsetsController(currentWindow, view)
      controller.isAppearanceLightStatusBars = !useDarkTheme // Set to true for light icons
    }
  }
}

@Composable
fun MindMateTheme(content: @Composable () -> Unit) {
  val themeOverride = ThemeSettings.themeOverride
  val darkTheme: Boolean =
    (isSystemInDarkTheme() || themeOverride.value == Theme.THEME_DARK) &&
      themeOverride.value != Theme.THEME_LIGHT

  StatusBarColorController(useDarkTheme = darkTheme)

  val colorScheme =
    when {
      darkTheme -> darkScheme
      else -> lightScheme
    }

  val customColorsPalette = if (darkTheme) darkCustomColors else lightCustomColors

  CompositionLocalProvider(LocalCustomColors provides customColorsPalette) {
    MaterialTheme(colorScheme = colorScheme, typography = AppTypography, content = content)
  }
}

// Deprecated: Use MindMateTheme instead
@Deprecated("Use MindMateTheme instead", ReplaceWith("MindMateTheme(content)"))
@Composable
fun GalleryTheme(content: @Composable () -> Unit) {
  MindMateTheme(content)
}
