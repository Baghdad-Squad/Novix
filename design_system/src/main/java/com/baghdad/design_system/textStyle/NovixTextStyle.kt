package com.baghdad.design_system.textStyle

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class NovixTextStyle(
    val headline: SizedTextStyle,
    val title: SizedTextStyle,
    val body: SizedTextStyle,
    val label: SizedTextStyle
){
    data class SizedTextStyle(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle
    )
}

val localNovixTextStyle = staticCompositionLocalOf { novixTextStyle }
