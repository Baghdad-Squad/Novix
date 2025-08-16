package com.baghdad.islamic_image_loader.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.baghdad.islamic_image_loader.cache.BlurStateCache
import com.baghdad.islamic_image_loader.model.ContentRestrictionTypes
import com.baghdad.islamic_image_loader.model.HaramImageDetector
import com.baghdad.islamic_image_loader.transformation.BlurHaramTransformation

@Composable
fun SafeImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loadingContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    onBlurContent: (@Composable () -> Unit)? = null,
    blurRadius: Dp = SafeImageDefaults.BlurRadius,
    contentScale: ContentScale = SafeImageDefaults.ContentScale,
    contentRestrictionTypes: ContentRestrictionTypes = ContentRestrictionTypes.STRICT
) {
    val context = LocalContext.current
    val blurCacheKey = "${imageUrl}_${contentRestrictionTypes.hashCode()}"
    var isBlurred by rememberSaveable(imageUrl) {
        mutableStateOf(BlurStateCache.getBlurState(blurCacheKey) ?: false)
    }

    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .transformations(
            BlurHaramTransformation(
                onBlur = { blurState ->
                    isBlurred = blurState
                    BlurStateCache.setBlurState(blurCacheKey, blurState)
                },
                haramImageDetector = HaramImageDetector(context),
                blurRadiusPx = blurRadius.value.toInt(),
                contentRestrictionTypes = contentRestrictionTypes
            )
        ).memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(policy = CachePolicy.ENABLED)
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
                this@SubcomposeAsyncImage.SubcomposeAsyncImageContent()
                onBlurContent?.let { content ->
                    if (isBlurred) {
                        content()
                    }
                }
            }

            is AsyncImagePainter.State.Loading -> {
                loadingContent?.let { content ->
                    content()
                }
            }

            is AsyncImagePainter.State.Error -> {
                errorContent?.let { content ->
                    content()
                }
            }

            else -> Unit
        }
    }
}

