package com.baghdad.design_system.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun GlassyButton(
    modifier: Modifier = Modifier,
    onAddBookmarkClick: () -> Unit = {},
    onGoBackClick: () -> Unit = {}
) {
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val rotation = if (isRtl) 180f else 0f

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ){

        if (isRtl) {
            IconButton(
                icon = com.baghdad.design_system.R.drawable.ic_arrow_left,
                onClick = onGoBackClick
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                icon = com.baghdad.design_system.R.drawable.ic_bookmark_add,
                onClick = onAddBookmarkClick,
                modifier = Modifier.rotate(rotation)
            )
        } else {
            IconButton(
                icon = com.baghdad.design_system.R.drawable.ic_bookmark_add,
                onClick = onAddBookmarkClick,
                modifier = Modifier.rotate(rotation)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                icon = com.baghdad.design_system.R.drawable.ic_arrow_left,
                onClick = onGoBackClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GlassyButtonPreview() {
    GlassyButton()
}