package com.baghdad.islamic_image_loader.model

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
    val assetFileDescriptor = context.assets.openFd(modelPath)
    FileInputStream(assetFileDescriptor.fileDescriptor).use { inputStream ->
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}

internal suspend fun predictGender(image: Bitmap, context: Context, modelPath: String): FloatArray =
    withContext(Dispatchers.Default) {
        val interpreter = Interpreter(loadModelFile(context, modelPath))
        val image = if (image.config == Bitmap.Config.HARDWARE) {
            image.copy(Bitmap.Config.ARGB_8888, false)
        } else image
        val inputImageSize = 128

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()

        val tensorImage = TensorImage.fromBitmap(image)
        val processedImage = imageProcessor.process(tensorImage)
        val outputBuffer =
            TensorBuffer.createFixedSize(intArrayOf(1, 2), DataType.FLOAT32)

        interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

        return@withContext outputBuffer.floatArray
    }
