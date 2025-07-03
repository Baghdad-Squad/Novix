package com.baghdad.design_system.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.baghdad.design_system.theme.Theme


@Composable
fun NovixThemeSelection(
    isDarkSelected: Boolean,
    onThemeSelected: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        ButtonSelection(
            selectedOption = "Dark",
            isSelected = isDarkSelected,
            onClick = { onThemeSelected(true) }
        )

        ButtonSelection(
            selectedOption = "Light",
            isSelected = !isDarkSelected,
            onClick = { onThemeSelected(false) }
        )
    }
}

@Composable
fun ButtonSelection(
    selectedOption: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) Theme.color.primaryVariant
        else Color.Transparent,
        animationSpec = tween(500)
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Theme.color.primary
        else Theme.color.stroke,
        animationSpec = tween(500)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                animatedBackgroundColor, RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
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
            text = selectedOption,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp),
            color = Theme.color.body,
            style = Theme.typography.label.large
        )

    }
}