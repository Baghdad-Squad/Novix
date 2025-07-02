package com.baghdad.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.baghdad.design_system.color.model.NovixColor
import com.baghdad.design_system.color.model.localNovixColor
import com.baghdad.design_system.textStyle.model.NovixTextStyle
import com.baghdad.design_system.textStyle.model.localNovixTextStyle

object Theme {
    val color: NovixColor
        @Composable @ReadOnlyComposable get() = localNovixColor.current
    val typography: NovixTextStyle
        @Composable @ReadOnlyComposable get() = localNovixTextStyle.current
}