package com.baghdad.design_system.component.button


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

@Composable
fun IconButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    tintIcon: Color = Theme.color.title,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Theme.color.backgroundLow)
            .size(40.dp)
            .border(
                width = 1.dp,
                color = Theme.color.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        Icon(
            painter = icon,
            contentDescription = stringResource(R.string.app_bar_icon),
            tint = tintIcon,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.Center)
        )
    }
}