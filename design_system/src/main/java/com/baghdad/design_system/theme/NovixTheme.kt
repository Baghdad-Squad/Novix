package com.baghdad.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.baghdad.design_system.color.darkThemeColor
import com.baghdad.design_system.color.lightThemeColor
import com.baghdad.design_system.color.localNovixColor
import com.baghdad.design_system.textStyle.localNovixTextStyle
import com.baghdad.design_system.textStyle.novixTextStyle

@Composable
fun NovixTheme(
    isDarkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val effectiveTheme = isDarkTheme ?: isSystemInDarkTheme()
    val theme = if (effectiveTheme) darkThemeColor else lightThemeColor

    CompositionLocalProvider(
        localNovixColor provides theme,
        localNovixTextStyle provides novixTextStyle,
        LocalIsDarkTheme provides effectiveTheme
    ) {
        content()
    }
}

val LocalIsDarkTheme = compositionLocalOf { true }