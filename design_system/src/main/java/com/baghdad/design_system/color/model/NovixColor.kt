package com.baghdad.design_system.color.model

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.baghdad.design_system.color.theme.lightThemeColor

data class NovixColor(
    val primaryColor: Primary,
    val secondaryColor: Color,
    val textColor: TextColor,
    val surfaceColor: SurfaceColor,
    val status: StatusColor
)

data class Primary(
    val normal: Color,
    val variant: Color,
)

data class TextColor(
    val title: Color,
    val body: Color,
    val hint: Color,
    val stroke: Color,
    val onPrimary: Color,
    val onPrimaryHint: Color,
    val iconBackground: Color,
    val iconBackgroundLow: Color,
    val backgroundLow: Color,
    val disable: Color
)

data class SurfaceColor(
    val surface: Color,
    val surfaceHigh: Color,
)

data class StatusColor(
    val redAccent: Color,
    val yellowAccent: Color,
    val greenAccent: Color,
    val greenVariant: Color,
)

val localNovixColor =  staticCompositionLocalOf{ lightThemeColor }
