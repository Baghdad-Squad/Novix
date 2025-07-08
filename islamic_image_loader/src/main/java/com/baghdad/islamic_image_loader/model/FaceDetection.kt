package com.baghdad.islamic_image_loader.model

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

fun detectAndCropFaces(inputBitmap: Bitmap, onFacesCropped: (List<Bitmap>) -> Unit) {
    val image = InputImage.fromBitmap(inputBitmap, 0)
    val detector = FaceDetection.getClient(buildFaceDetectionOptions())

    detector.process(image)
        .addOnSuccessListener { faces ->
            val croppedFaces = faces.take(5).mapNotNull { face ->
                try {
                    cropFace(inputBitmap, face)
                } catch (_: Exception) {
                    null
                }
            }
            onFacesCropped(croppedFaces)
        }
        .addOnFailureListener {
            onFacesCropped(emptyList())
        }
}

private fun buildFaceDetectionOptions(): FaceDetectorOptions {
    return FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .build()
}

private fun cropFace(inputBitmap: Bitmap, face: Face): Bitmap {
    val bounds: Rect = face.boundingBox
    val safeBounds = createSafeBoundsRectangle(bounds = bounds, inputBitmap = inputBitmap)
    return createCroppedFaceBitmap(inputBitmap, safeBounds)
}

private fun createSafeBoundsRectangle(bounds: Rect, inputBitmap: Bitmap): Rect {
    return Rect(
        bounds.left.coerceAtLeast(0),
        bounds.top.coerceAtLeast(0),
        bounds.right.coerceAtMost(inputBitmap.width),
        bounds.bottom.coerceAtMost(inputBitmap.height)
    )
}

private fun createCroppedFaceBitmap(inputBitmap: Bitmap, safeBounds: Rect): Bitmap {
    return Bitmap.createBitmap(
        inputBitmap,
        safeBounds.left,
        safeBounds.top,
        safeBounds.width(),
        safeBounds.height()
    )
}