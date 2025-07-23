package com.baghdad.design_system.component.islamicImage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.islamic_image_loader.ui.SafeImage

@Composable
fun IslamicImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    SafeImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        onBlurContent = {
            BlurImageContent(modifier = modifier)
        },
        loadingContent = {
            WavyLoadingIndicator()
        },
        errorContent = {
            ErrorImageContent(modifier = modifier)
        }
    )
}


