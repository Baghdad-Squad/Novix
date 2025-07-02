package com.baghdad.design_system.color.theme

import androidx.compose.ui.graphics.Color
import com.baghdad.design_system.color.model.*

val darkThemeColor = NovixColor(
    primary = Primary(
        normal = Color(0xFFC65A42),
        variant = Color(0xFF1F0E0A)
    ),
    secondary = Color(0xFF4B0412),
    text = TextColor(
        title = Color(0xDEFFFFFF),
        body = Color(0x99FFFFFF),
        hint = Color(0x61FFFFFF),
        stroke = Color(0x14FFFFFF),
        onPrimary = Color(0xDEFFFFFF),
        onPrimaryHint = Color(0x61FFFFFF),
        iconBackground = Color(0xB2000000),
        iconBackgroundLow = Color(0x1FFFFFFF),
        backgroundLow = Color(0x08FFFFFF),
        disable = Color(0xFF1F1C1B)
    ),
    surface = SurfaceColor(
        surface = Color(0xFF0D0608),
        surfaceHigh = Color(0xFF110E0F)
    ),
    status = StatusColor(
        redAccent = Color(0xFFF75662),
        yellowAccent = Color(0xFFCFC657),
        greenAccent = Color(0xFF19744D),
        greenVariant = Color(0xFF1D1F1E)
    )
)
