package com.baghdad.islamic_image_loader.model

import android.content.Context
import android.graphics.Bitmap
import com.baghdad.islamic_image_loader.ml.IslamicImageModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class HaramImageDetector(
    private val context: Context
) {
    private val inputImageSize = INPUT_IMAGE_SIZE

    fun isImageHaram(
        selectedBitmap: Bitmap,
        contentRestrictionTypes: ContentRestrictionTypes = ContentRestrictionTypes.STRICT
    ): Boolean {
        val model = IslamicImageModel.newInstance(context)

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(selectedBitmap)

        val imageProcessor = buildImageProcessor()
        val processedImage = imageProcessor.process(tensorImage)

        val inputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, inputImageSize, inputImageSize, 3), DataType.FLOAT32
        )
        inputBuffer.loadBuffer(processedImage.buffer)

        val outputBuffer = model.process(inputBuffer).outputFeature0AsTensorBuffer
        model.close()

        val result = outputBuffer.floatArray
        result.getOrNull(0) ?: 0f
        val nudeScore = result.getOrNull(1) ?: 0f
        return nudeScore > contentRestrictionTypes.thresholds
    }

    private fun buildImageProcessor(): ImageProcessor {
        return ImageProcessor.Builder()
            .add(ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
    }

    companion object {
        private const val INPUT_IMAGE_SIZE = 224
    }

}