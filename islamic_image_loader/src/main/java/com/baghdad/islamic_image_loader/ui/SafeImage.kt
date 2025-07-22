package com.baghdad.islamic_image_loader.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.baghdad.islamic_image_loader.R
import com.baghdad.islamic_image_loader.model.HaramImageDetector
import com.baghdad.islamic_image_loader.transformation.BlurHaramTransformation

@Composable
fun SafeImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = SafeImageDefaults.LoadingContent,
    errorContent: @Composable () -> Unit = SafeImageDefaults.ErrorContent,
    onBlurContent: (@Composable () -> Unit)? = null,
    blurRadius: Dp = SafeImageDefaults.BlurRadius,
    contentScale: ContentScale = SafeImageDefaults.ContentScale
) {
    val context = LocalContext.current
    var isBlurred by remember { mutableStateOf(true) }
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .transformations(
            BlurHaramTransformation(
                onBlur = {
                    isBlurred = it
                },
                haramImageDetector = HaramImageDetector(context),
                blurRadiusPx = blurRadius.value.toInt()
            )
        )
        .crossfade(true)
        .build()
    SubcomposeAsyncImage(
        modifier = modifier,
        model = request,
        contentDescription = contentDescription,
        contentScale = contentScale
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Success -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    this@SubcomposeAsyncImage.SubcomposeAsyncImageContent()
                    onBlurContent?.let { content ->
                        if (isBlurred) {
                            content()
                        }
                    }
                }
            }

            is AsyncImagePainter.State.Loading -> {
                loadingContent()
            }

            is AsyncImagePainter.State.Error -> {
                errorContent()
            }

            else -> Unit
        }
    }
}

@Composable
fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_defualt_image),
        contentDescription = "Default Image",
        modifier = modifier.size(56.dp)
    )
}

