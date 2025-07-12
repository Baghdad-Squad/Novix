package com.baghdad.islamic_image_loader.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.baghdad.islamic_image_loader.utils.convertBitmapToSoftwareBitmap
import com.baghdad.islamic_image_loader.utils.loadModelFile
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.min

fun detectFaces(
    context: Context,
    inputBitmap: Bitmap,
    modelName: String = "blaze_face_short_range.tflite",
    maxFaces: Int = 5,
    scoreThreshold: Float = 0.5f,
    resizedWidth: Int = 128,
    resizedHeight: Int = 128,
    usePreprocessing: Boolean = true,
    onFacesCropped: (List<Bitmap>) -> Unit
) {
    try {
        val inputBitmap = convertBitmapToSoftwareBitmap(inputBitmap)
        val interpreter = Interpreter(loadModelFile(context, modelName))

        val bitmap = if (usePreprocessing) {
            val tensorImage = TensorImage.fromBitmap(inputBitmap)
            val resizeOp = ResizeOp(resizedWidth, resizedHeight, ResizeOp.ResizeMethod.BILINEAR)
            resizeOp.apply(tensorImage).bitmap
        } else {
            inputBitmap.createScaledBitmap(resizedWidth, resizedHeight)
        }

        val inputBuffer = preprocessImage(bitmap)

        val outputBoxes = Array(1) { Array(896) { FloatArray(16) } }
        val outputScores = Array(1) { Array(896) { FloatArray(1) } }

        interpreter.runForMultipleInputsOutputs(
            arrayOf(inputBuffer),
            mapOf(0 to outputBoxes, 1 to outputScores)
        )

        val croppedFaces = mutableListOf<Bitmap>()

        for (i in 0 until 896) {
            val score = outputScores[0][i][0]
            if (score < scoreThreshold) continue

            val box = outputBoxes[0][i]
            val (xMin, yMin, xMax, yMax) = getPixelBoundingBox(box, inputBitmap)

            val safeRect = createSafeBoundsRectangle(Rect(xMin, yMin, xMax, yMax), inputBitmap)
            if (safeRect.width() > 10 && safeRect.height() > 10) {
                try {
                    val cropped = createCroppedFaceBitmap(inputBitmap, safeRect)
                    croppedFaces.add(cropped)
                    if (croppedFaces.size >= maxFaces) break
                } catch (_: Exception) {

                }
            }
        }

        interpreter.close()
        onFacesCropped(croppedFaces)

    } catch (e: Exception) {
        Log.e("FaceDetection", "Error in face detection", e)
        onFacesCropped(emptyList())
    }
}

private fun Bitmap.createScaledBitmap(width: Int, height: Int): Bitmap =
    Bitmap.createScaledBitmap(this, width, height, true)

private fun getPixelBoundingBox(box: FloatArray, bitmap: Bitmap): List<Int> {
    val (xCenter, yCenter, width, height) = box.take(4)
    val xMin = ((xCenter - width / 2f) * bitmap.width).toInt()
    val yMin = ((yCenter - height / 2f) * bitmap.height).toInt()
    val xMax = ((xCenter + width / 2f) * bitmap.width).toInt()
    val yMax = ((yCenter + height / 2f) * bitmap.height).toInt()
    return listOf(xMin, yMin, xMax, yMax)
}

private fun createSafeBoundsRectangle(bounds: Rect, bitmap: Bitmap): Rect {
    val left = max(0, bounds.left)
    val top = max(0, bounds.top)
    val right = min(bitmap.width, bounds.right)
    val bottom = min(bitmap.height, bounds.bottom)
    return Rect(left, top, right, bottom)
}

private fun createCroppedFaceBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
    return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())
}

private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
    val inputSize = 128
    val intValues = IntArray(inputSize * inputSize)
    val byteBuffer = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4)
    byteBuffer.order(ByteOrder.nativeOrder())

    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

    scaledBitmap.getPixels(intValues, 0, inputSize, 0, 0, inputSize, inputSize)

    for (pixel in intValues) {
        val r = ((pixel shr 16) and 0xFF) / 255.0f
        val g = ((pixel shr 8) and 0xFF) / 255.0f
        val b = (pixel and 0xFF) / 255.0f
        byteBuffer.putFloat(r)
        byteBuffer.putFloat(g)
        byteBuffer.putFloat(b)
    }

    byteBuffer.rewind()
    return byteBuffer
}