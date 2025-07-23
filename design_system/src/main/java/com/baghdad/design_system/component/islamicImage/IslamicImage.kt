package com.baghdad.design_system.component.islamicImage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.baghdad.islamic_image_loader.ui.SafeImage

@Composable
fun IslamicImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    SafeImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        onBlurContent = {
            BlurImageContent()
        },
        loadingContent = {
            LoadingImageContent()
        },
        errorContent = {
            ErrorImageContent()
        },
        contentScale = contentScale
    )
}


