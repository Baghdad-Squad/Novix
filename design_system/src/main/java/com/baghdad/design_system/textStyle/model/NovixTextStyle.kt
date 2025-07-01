package com.baghdad.design_system.textStyle.model

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.baghdad.design_system.textStyle.theme.novixTextStyle

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
