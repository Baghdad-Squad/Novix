package com.baghdad.islamic_image_loader.utils

import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp

fun buildImageProcessor(inputImageSize: Int): ImageProcessor {
    return ImageProcessor.Builder()
        .add(ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0f, 255f))
        .build()
}