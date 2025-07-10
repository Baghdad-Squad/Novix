package com.baghdad.design_system.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class NovixColor(
    val primary: Color,
    val primaryVariant: Color,
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
