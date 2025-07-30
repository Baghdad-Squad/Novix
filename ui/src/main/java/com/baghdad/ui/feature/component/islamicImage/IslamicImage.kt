package com.baghdad.ui.feature.component.islamicImage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.baghdad.islamic_image_loader.ui.SafeImage

@Composable
fun IslamicImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    onBlurContent: @Composable () -> Unit = { BlurImageContent() },
    loadingContent: @Composable () -> Unit = { LoadingImageContent() },
    errorContent: @Composable () -> Unit = { ErrorImageContent() },
    contentScale: ContentScale = ContentScale.Crop,
) {
    SafeImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        onBlurContent = onBlurContent,
        loadingContent = loadingContent,
        errorContent = errorContent,
        contentScale = contentScale
    )
}