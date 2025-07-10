package com.baghdad.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.baghdad.design_system.color.darkThemeColor
import com.baghdad.design_system.color.lightThemeColor
import com.baghdad.design_system.color.localNovixColor
import com.baghdad.design_system.textStyle.localNovixTextStyle
import com.baghdad.design_system.textStyle.novixTextStyle

@Composable
fun NovixTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val theme = if (isDarkTheme) darkThemeColor else lightThemeColor

    CompositionLocalProvider(
        localNovixColor provides theme,
        localNovixTextStyle provides novixTextStyle
    ) {
        content()
    }
}