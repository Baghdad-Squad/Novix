package com.baghdad.ui.feature.onBoarding.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.dropShadow
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun SkipText(onClick: () -> Unit) {
    val color = Theme.color.primary.copy(0.4f)
    Text(
        text = stringResource(R.string.skip),
        color = Theme.color.primary,
        style = Theme.typography.label.medium,
        modifier = Modifier.padding(top = 56.dp)
    )
    Box(
        modifier = Modifier
            .offset(x = (-50).dp, y = (-100).dp)
            .size(150.dp)
            .dropShadow(
                CircleShape,
                color = color,
                alpha = 0.4f,
                blur = 150.dp,
                offsetY = -24.dp,
                offsetX = 0.dp,
                spread = 20.dp,
            )
            .noRippleClickable { onClick() }
    )
}