package com.baghdad.design_system.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun Chip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(
        targetState = isSelected,
        label = stringResource(R.string.chip_animation)
    )
    val backgroundColor by transition.animateColor(
        targetValueByState = { if (it) Theme.color.secondary else Color.Transparent },
        label = stringResource(R.string.background_color_animation),
        transitionSpec = {
            TweenSpec(durationMillis = 300)
        })
    val paddingValue by transition.animateDp(
        targetValueByState = { if (it) 24.dp else 12.dp },
        label = stringResource(R.string.padding_value_animation),
        transitionSpec = {
            TweenSpec(durationMillis = 300)
        },
    )
    val radius by transition.animateDp(
        targetValueByState = { if (it) 12.dp else 8.dp },
        label = stringResource(R.string.radius_animation),
        transitionSpec = {
            TweenSpec(durationMillis = 300)
        },
    )
    val textColor by transition.animateColor(
        targetValueByState = { if (it) Theme.color.onPrimary else Theme.color.body },
        label = stringResource(R.string.text_color_animation),
        transitionSpec = {
            TweenSpec(durationMillis = 300)
        },
    )
    Text(
        title,
        style = Theme.typography.label.medium,
        color = textColor,
        modifier = modifier
            .noRippleClickable {
                onClick()
            }
            .background(color = backgroundColor, shape = RoundedCornerShape(radius))
            .padding(vertical = 8.dp, horizontal = paddingValue)
    )

}


@Preview(showBackground = true)
@Composable
private fun PreviewChip() {
    NovixTheme(
        isDarkTheme = false
    ) {
        Chip(
            "All",
            isSelected = true,
            onClick = { },
        )
    }
}