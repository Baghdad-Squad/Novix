package com.baghdad.design_system.component.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun OutlinedButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isEnabled) Theme.color.stroke else Theme.color.disable,
        animationSpec = tween(300),
        label = stringResource(R.string.outlined_button_border_color)
    )
    OutlinedButton(
        modifier = modifier.height(48.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = Theme.color.primary,
            disabledContentColor = Theme.color.disable,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = animatedBorderColor
        ),
        enabled = isEnabled && !isLoading,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Text(
            text = label,
            style = Theme.typography.label.large
        )
        AnimatedContent(
            modifier = Modifier.padding(start = 8.dp),
            targetState = isLoading
        ) { loading ->
            if (loading) {
                StripedCircularProgressIndicator(
                    color = LocalContentColor.current
                )
            } else {
                painter?.let { painter ->
                    Icon(
                        painter = painter,
                        contentDescription = stringResource(R.string.primary_button_icon),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }
    }
}