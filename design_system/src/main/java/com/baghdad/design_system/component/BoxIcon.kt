package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

@Composable
fun BoxIcon(
    modifier: Modifier = Modifier,
    icon: Painter,
    contentDescription: String = "Icon",
    tint: Color = Theme.color.title,
    background: Color = Theme.color.iconBackgroundLow,
    onClick: () -> Unit = {},

    ) {
    Box(
        modifier
            .size(40.dp)
            .background(
                color = background,
                shape = RoundedCornerShape(12.dp)
            )
            .noRippleClickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = tint
        )
    }
}