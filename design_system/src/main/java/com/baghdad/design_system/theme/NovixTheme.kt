package com.baghdad.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.baghdad.design_system.color.darkThemeColor
import com.baghdad.design_system.color.lightThemeColor
import com.baghdad.design_system.color.localNovixColor
import com.baghdad.design_system.drawables.darkThemeDrawables
import com.baghdad.design_system.drawables.lightThemeDrawables
import com.baghdad.design_system.drawables.localNovixDrawables
import com.baghdad.design_system.textStyle.localNovixTextStyle
import com.baghdad.design_system.textStyle.novixTextStyle

@Composable
fun NovixTheme(
    isDarkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val effectiveTheme = isDarkTheme ?: isSystemInDarkTheme()
    val themeColors = if (effectiveTheme) darkThemeColor else lightThemeColor
    val themeDrawables = if (effectiveTheme) darkThemeDrawables else lightThemeDrawables

    CompositionLocalProvider(
        localNovixColor provides themeColors,
        localNovixTextStyle provides novixTextStyle,
        localNovixDrawables provides themeDrawables,
    ) {
        content()
    }
}