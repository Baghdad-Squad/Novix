package com.baghdad.design_system.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.shared.Selectable
import com.baghdad.design_system.theme.Theme


@Composable
fun Selection(
    option: Selectable<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    trailingText: String? = null,
) {
    val transition = updateTransition(
        targetState = option.isSelected,
        label = "SelectionTransition"
    )

    val animatedBackgroundColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = "BackgroundColorAnimation"
    ) { isSelected ->
        if (isSelected) Theme.color.primaryVariant
        else Theme.color.surface.copy(alpha = 0.0f)
    }

    val animatedBorderColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = "BorderColorAnimation"
    ) { isSelected ->
        if (isSelected) Theme.color.primary
        else Theme.color.stroke
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(animatedBackgroundColor, RoundedCornerShape(8.dp))
            .border(
                width = 1.5f.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .wrapContentHeight()
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = option.value,
                color = Theme.color.body,
                style = Theme.typography.label.large
            )

            description?.let {
                Text(
                    text = it,
                    color = Theme.color.hint,
                    style = Theme.typography.label.small,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        trailingText?.let {
            Text(
                text = trailingText,
                modifier = Modifier.padding(end = 12.dp),
                color = Theme.color.hint,
                style = Theme.typography.label.small
            )
        }
    }
}