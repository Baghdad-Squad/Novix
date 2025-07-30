package com.baghdad.ui.feature.component.islamicImage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.baghdad.islamic_image_loader.ui.SafeImage

@Composable
fun IslamicImage(
    imageUrl: String,
    contentDescription: String?,
    isLoadingEnabled: Boolean = true,
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
        loadingContent =
            if (isLoadingEnabled) {
                {
                    LoadingImageContent()
                }
            } else {
                null
            },
        errorContent = {
            ErrorImageContent()
        },
        contentScale = contentScale,
    )
}
