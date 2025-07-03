package com.baghdad.islamic_image_loader.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.Image
import coil3.toBitmap
import com.baghdad.islamic_image_loader.model.ImageUrlLoader
import com.baghdad.islamic_image_loader.model.detectAndCropFace
import com.baghdad.islamic_image_loader.model.predictGender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SafeImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var image by remember { mutableStateOf<Image?>(null) }
    var gender by remember { mutableStateOf<String?>(null) }
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
                            gender = if (prediction[0] > prediction[1]) "Male" else "Female"
                        } catch (e: Exception) {
                            Log.e("SafeImage", "Gender detection failed", e)
                            gender = "Unknown"
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

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!isLoading && image != null) {
                Image(
                    painter = image!!.asPainter(context),
                    contentDescription = null,
                    modifier = Modifier.blur(radius = 16.dp),
                )
                gender?.let {
                    BasicText(text = it)
                }
            }
        }
    }
}

