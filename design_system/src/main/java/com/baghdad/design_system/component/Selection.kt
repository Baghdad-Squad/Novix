package com.baghdad.design_system.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.shared.Selectable
import com.baghdad.design_system.theme.Theme


@Composable
fun NovixSelection(
    option: Selectable<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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

    Box(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Selection() {
    NovixSelection(
        option = Selectable("Option 3", isSelected = false),
        onClick = { /* Handle click */ },
        modifier = Modifier.padding(16.dp)
    )
}