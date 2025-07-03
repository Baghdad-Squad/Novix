package com.baghdad.islamic_image_loader.component

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.Image
import coil3.compose.asPainter
import coil3.toBitmap
import com.baghdad.islamic_image_loader.model.ImageUrlLoader
import com.baghdad.islamic_image_loader.model.detectAndCropFace
import com.baghdad.islamic_image_loader.model.predictGender
import com.example.islamic_library.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SafeImage(
    imageUrl: String,
    contentDescription: String?
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var image by remember { mutableStateOf<Image?>(null) }
    var isMale by remember { mutableStateOf<Boolean?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imageUrl) {
        val loadedImage = ImageUrlLoader(imageUrl, context)
        image = loadedImage

        loadedImage?.let {
            detectAndCropFace(it.toBitmap()) { croppedFace ->
                if (croppedFace != null) {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val prediction =
                                predictGender(croppedFace, context, "model_lite_gender_q.tflite")
                            isMale = (prediction[0] > prediction[1])
                        } catch (e: Exception) {
                            Log.e("SafeImage", "Gender detection failed", e)
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    Log.w("SafeImage", "No face detected, using full image.")
                    isLoading = false
                }
            }
        }
    }

    Box(modifier = modifier) {
        if (!isLoading && (isMale != null)) {
            Image(
                painter = image!!.asPainter(context),
                contentDescription,
                modifier = if (!isMale!!) Modifier.blur(radius = 16.dp) else Modifier,
            )
        } else {
            Image(
                painter = R.drawable.img_defualt_image,
                contentDescription = "Default Image",
                modifier = Modifier.blur(radius = 16.dp),
            )
        }
    }
}

