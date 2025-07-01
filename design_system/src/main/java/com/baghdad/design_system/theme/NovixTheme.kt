package com.baghdad.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.baghdad.design_system.color.model.localNovixColor
import com.baghdad.design_system.color.theme.darkThemeColor
import com.baghdad.design_system.color.theme.lightThemeColor
import com.baghdad.design_system.textStyle.model.localNovixTextStyle
import com.baghdad.design_system.textStyle.theme.novixTextStyle

@Composable
fun NovixTheme(
    isDarkTheme: Boolean = false,
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