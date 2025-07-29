package com.baghdad.design_system.component.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.theme.Theme

@Composable
fun FloatingActionButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Theme.color.primary,
            contentColor = Theme.color.onPrimary,
            disabledContainerColor = Theme.color.disable,
            disabledContentColor = Theme.color.onPrimaryHint
        ),
        enabled = isEnabled && !isLoading,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.size(56.dp)
    ) {
        AnimatedContent(isLoading) { loading ->
            if (loading) {
                StripedCircularProgressIndicator(
                    color = LocalContentColor.current
                )
            } else {
                Icon(
                    painter = painter,
                    contentDescription = stringResource(R.string.floating_action_button_icon),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}