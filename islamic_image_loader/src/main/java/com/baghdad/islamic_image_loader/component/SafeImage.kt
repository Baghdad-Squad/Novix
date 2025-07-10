package com.baghdad.islamic_image_loader.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.Image
import coil3.compose.asPainter
import coil3.toBitmap
import com.baghdad.islamic_image_loader.R
import com.baghdad.islamic_image_loader.model.detectAndCropFaces
import com.baghdad.islamic_image_loader.model.imageUrlLoader
import com.baghdad.islamic_image_loader.model.predictGender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SafeImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeHolder: @Composable (modifier: Modifier) -> Unit = { ImagePlaceholder(it) },
    blur: Dp = 16.dp,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var image by remember { mutableStateOf<Image?>(null) }
    var shouldBlur by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imageUrl) {
        try {
            val loadedImage = imageUrlLoader(imageUrl, context)
            image = loadedImage

            if (loadedImage != null) {
                detectAndCropFaces(loadedImage.toBitmap()) { croppedFaces ->
                    if (croppedFaces.isNotEmpty()) {
                        coroutineScope.launch {
                            val hasFemaleFace = withContext(Dispatchers.IO) {
                                checkForFemales(croppedFaces, context)
                            }
                            shouldBlur = hasFemaleFace
                            isLoading = false
                        }
                    } else {
                        shouldBlur = false
                        isLoading = false
                    }
                }
            } else {
                isLoading = false
            }
        } catch (_: Exception) {
            isLoading = false
        }
    }

    Box(modifier = modifier) {
        when {
            !isLoading && image != null -> {
                Image(
                    painter = image!!.asPainter(context),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = if (shouldBlur) Modifier.blur(blur) else Modifier
                )
            }

            else -> {
                placeHolder(Modifier.align(Alignment.Center))
            }
        }
    }
}

private suspend fun checkForFemales(
    faces: List<coil3.Bitmap>,
    context: android.content.Context
): Boolean {
    return try {
        for (face in faces) {
            try {
                val prediction = predictGender(
                    image = face,
                    context = context,
                    modelPath = "model_lite_gender_q.tflite"
                )
                if (prediction[1] > prediction[0]) {
                    return true
                }
            } catch (_: Exception) {
                continue
            }
        }
        false
    } catch (_: Exception) {
        false
    }
}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_defualt_image),
        contentDescription = "Default Image",
        modifier = modifier.size(56.dp)
    )
}