package com.baghdad.design_system

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val backgroundColor by animateColorAsState(
        if (isSelected) Theme.color.secondary else Theme.color.surface, label = "background color"
    )
    val paddingValue by animateDpAsState(if (isSelected) 24.dp else 12.dp)
    val radius by animateDpAsState(if (isSelected) 12.dp else 8.dp)
    Text(
        title,
        style = Theme.typography.label.medium,
        color = if (isSelected) Theme.color.onPrimary else Theme.color.body,
        modifier = modifier
            .noRippleClickable {
                onClick()
            }
            .background(backgroundColor, shape = RoundedCornerShape(radius))
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