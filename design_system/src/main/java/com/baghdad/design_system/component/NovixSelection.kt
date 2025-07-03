package com.baghdad.design_system.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.shared.Selectable
import com.baghdad.design_system.theme.Theme

@Composable
fun NovixSelection(
    option: Selectable <String>,
    onClick: () -> Unit
) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (option.isSelected) Theme.color.primaryVariant
        else Color.Transparent,
        animationSpec = tween(500)
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (option.isSelected) Theme.color.primary
        else Theme.color.stroke,
        animationSpec = tween(500)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(animatedBackgroundColor, RoundedCornerShape(8.dp))
            .border(
                width = 1.5f.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .wrapContentHeight()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
            ) {
                onClick()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = option.value,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            color = Theme.color.body,
            style = Theme.typography.label.large
        )
    }
}