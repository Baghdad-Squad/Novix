package com.baghdad.design_system.component.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = isEnabled,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        colors = buttonColors(
            containerColor = Theme.color.primary,
            contentColor = Theme.color.onPrimary,
            disabledContainerColor = Theme.color.disable,
            disabledContentColor = Theme.color.onPrimaryHint
        ),
        shape = RoundedCornerShape(12.dp),
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