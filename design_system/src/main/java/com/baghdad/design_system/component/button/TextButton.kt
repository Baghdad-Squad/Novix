package com.baghdad.design_system.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun TextButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    val animatedContentColor by animateColorAsState(
        targetValue = if (isEnabled) Theme.color.primary else Theme.color.disable,
        animationSpec = tween(300),
        label = stringResource(R.string.text_button_content_color)
    )
    Row(
        modifier = modifier.clickable(enabled = isEnabled) {
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = Theme.typography.label.large.copy(
                color = animatedContentColor
            ),
        )
        AnimatedVisibility(
            modifier = Modifier.padding(start = 4.dp),
            visible = isLoading
        ) {
            StripedCircularProgressIndicator(
                color = animatedContentColor
            )
        }
    }
}