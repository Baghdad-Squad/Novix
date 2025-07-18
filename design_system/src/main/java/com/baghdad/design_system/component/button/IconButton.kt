package com.baghdad.design_system.component.button


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun IconButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    background: Color = Theme.color.iconBackgroundLow,
    tintIcon: Color = Theme.color.title,
    borderStroke: BorderStroke? = BorderStroke(width = 1.dp, Theme.color.stroke),
    shape: Shape = RoundedCornerShape(12.dp),
    size: Pair<Dp, Dp> = Pair(48.dp, 48.dp),
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = background)
            .border(
                width = 1.dp,
                color = Theme.color.stroke,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(shape)
            .size(width = size.first, height = size.second)
            .then(
                if (borderStroke == null) Modifier else Modifier.border(borderStroke, shape = shape)
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

@Composable
@Preview(showBackground = true)
private fun IconButtonPreview() {
    NovixTheme(isDarkTheme = true) {
        IconButton(
            icon = painterResource(R.drawable.ic_star),
            onClick = { /* Do something */ },
            modifier = Modifier,
            background = Theme.color.primary,
            tintIcon = Theme.color.surface,
            borderStroke = BorderStroke(1.dp, Theme.color.stroke),
        )
    }
}