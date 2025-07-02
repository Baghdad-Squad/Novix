package com.baghdad.design_system.color.model

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.baghdad.design_system.color.theme.lightThemeColor

data class NovixColor(
    val normal: Color,
    val variant: Color,
    val secondary: Color,
    val title: Color,
    val body: Color,
    val hint: Color,
    val stroke: Color,
    val onPrimary: Color,
    val onPrimaryHint: Color,
    val iconBackground: Color,
    val iconBackgroundLow: Color,
    val backgroundLow: Color,
    val disable: Color,
    val surface: Color,
    val surfaceHigh: Color,
    val redAccent: Color,
    val yellowAccent: Color,
    val greenAccent: Color,
    val greenVariant: Color,
)

val localNovixColor = staticCompositionLocalOf { lightThemeColor }
