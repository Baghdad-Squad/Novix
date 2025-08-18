package com.baghdad.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.baghdad.design_system.color.NovixColor
import com.baghdad.design_system.color.localNovixColor
import com.baghdad.design_system.drawables.NovixDrawables
import com.baghdad.design_system.drawables.localNovixDrawables
import com.baghdad.design_system.textStyle.NovixTextStyle
import com.baghdad.design_system.textStyle.localNovixTextStyle

object Theme {
    val color: NovixColor
        @Composable @ReadOnlyComposable get() = localNovixColor.current
    val typography: NovixTextStyle
        @Composable @ReadOnlyComposable get() = localNovixTextStyle.current
    val drawable: NovixDrawables
        @Composable @ReadOnlyComposable
        get() = localNovixDrawables.current
}
