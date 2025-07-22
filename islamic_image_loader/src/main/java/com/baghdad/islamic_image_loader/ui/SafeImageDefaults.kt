package com.baghdad.islamic_image_loader.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale as ComposeContentScale

object SafeImageDefaults {
    val LoadingContent: @Composable () -> Unit = { ImagePlaceholder() }
    val ErrorContent: @Composable () -> Unit = { ImagePlaceholder() }
    val BlurRadius = 16.dp
    val ContentScale = ComposeContentScale.Crop
}