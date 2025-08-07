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
import com.baghdad.design_system.component.button.TextButton
import com.baghdad.design_system.modifier.dropShadow
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun SkipText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = Theme.color.primary
    TextButton(
        label = stringResource(R.string.skip),
        onClick = onClick,
        noRipple = true,
        modifier = modifier
            .padding(top = 56.dp)
    )

    Box(
        modifier = Modifier
            .offset(x = (-50).dp, y = (-100).dp)
            .size(150.dp)
            .dropShadow(
                CircleShape,
                color = color,
                alpha = 0.2f,
                blur = 100.dp,
                spread = 2.dp,
            )
    )
}