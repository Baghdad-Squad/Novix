package com.baghdad.design_system.component.islamicImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ErrorImageContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Image(
            painter = painterResource(com.baghdad.islamic_image_loader.R.drawable.img_defualt_image),
            contentDescription = "Default Image",
            modifier = modifier.size(56.dp)
        )
    }
}