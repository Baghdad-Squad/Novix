package com.baghdad.islamic_image_loader.model

import android.content.Context
import android.graphics.Bitmap
import com.baghdad.islamic_image_loader.utils.buildImageProcessor
import com.baghdad.islamic_image_loader.utils.convertBitmapToSoftwareBitmap
import com.baghdad.islamic_image_loader.utils.loadModelFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


internal suspend fun predictGender(
    image: Bitmap,
    context: Context,
    modelPath: String,
    inputImageSize: Int = 128
): FloatArray =
    withContext(Dispatchers.Default) {
        val interpreter = Interpreter(loadModelFile(context, modelPath))
        val image = convertBitmapToSoftwareBitmap(image)
        val imageProcessor = buildImageProcessor(inputImageSize)
        val tensorImage = TensorImage.fromBitmap(image)
        val processedImage = imageProcessor.process(tensorImage)
        val outputBuffer =
            TensorBuffer.createFixedSize(intArrayOf(1, 2), DataType.FLOAT32)

        interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

        return@withContext outputBuffer.floatArray
    }
