package com.baghdad.ui.feature.component.islamicImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun ErrorImageContent() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Theme.drawable.imagePlaceholder),
            contentDescription = "Default Image",
            modifier = Modifier.size(56.dp)
        )
    }
}