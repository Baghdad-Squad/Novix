package com.baghdad.islamic_image_loader.model

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.baghdad.islamic_image_loader.utils.buildImageProcessor
import com.baghdad.islamic_image_loader.utils.convertBitmapToSoftwareBitmap
import com.baghdad.islamic_image_loader.utils.loadModelFile
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
fun detectFaces(
    context: Context,
    inputBitmap: Bitmap,
    modelName: String = "blaze_face_short_range.tflite",
    maxFaces: Int = 5,
    scoreThreshold: Float = 0.5f,
    inputImageSize: Int = 128,
    onFacesCropped: (List<Bitmap>) -> Unit
) {
    try {
        val softwareBitmap = convertBitmapToSoftwareBitmap(inputBitmap)
        val interpreter = Interpreter(loadModelFile(context, modelName))
        val tensorImage = TensorImage.fromBitmap(softwareBitmap)
        val imageProcessor = buildImageProcessor(inputImageSize = inputImageSize)
        val processedImage = imageProcessor.process(tensorImage)
        val inputBuffer: ByteBuffer = processedImage.buffer

        val outputBoxes = Array(1) { Array(896) { FloatArray(16) } }
        val outputScores = Array(1) { Array(896) { FloatArray(1) } }

        interpreter.runForMultipleInputsOutputs(
            arrayOf(inputBuffer),
            mapOf(0 to outputBoxes, 1 to outputScores)
        )

        val croppedFaces = processDetections(
            outputBoxes[0],
            outputScores[0],
            softwareBitmap,
            scoreThreshold,
            maxFaces
        )

        interpreter.close()
        onFacesCropped(croppedFaces)

    } catch (e: Exception) {
        onFacesCropped(emptyList())
    }
}

private fun processDetections(
    boxes: Array<FloatArray>,
    scores: Array<FloatArray>,
    bitmap: Bitmap,
    scoreThreshold: Float,
    maxFaces: Int
): List<Bitmap> {
    val bitmapWidth = bitmap.width
    val bitmapHeight = bitmap.height
    val croppedFaces = mutableListOf<Bitmap>()

    val validIndices = scores.indices.filter { scores[it][0] >= scoreThreshold }

    for (i in validIndices) {
        if (croppedFaces.size >= maxFaces) break

        val box = boxes[i]
        val (xCenter, yCenter, width, height) = box

        val halfWidth = width * 0.5f
        val halfHeight = height * 0.5f

        val xMin = max(0, ((xCenter - halfWidth) * bitmapWidth).toInt())
        val yMin = max(0, ((yCenter - halfHeight) * bitmapHeight).toInt())
        val xMax = min(bitmapWidth, ((xCenter + halfWidth) * bitmapWidth).toInt())
        val yMax = min(bitmapHeight, ((yCenter + halfHeight) * bitmapHeight).toInt())

        val rectWidth = xMax - xMin
        val rectHeight = yMax - yMin

        if (rectWidth > 10 && rectHeight > 10) {
            try {
                val cropped = Bitmap.createBitmap(bitmap, xMin, yMin, rectWidth, rectHeight)
                croppedFaces.add(cropped)
            } catch (_: Exception) {

            }
        }
    }

    return croppedFaces
}