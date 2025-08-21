package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun CircleDot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(start = 8.dp, end = 10.dp)
            .size(3.dp)
            .background(Theme.color.hint, CircleShape)
    )
}
