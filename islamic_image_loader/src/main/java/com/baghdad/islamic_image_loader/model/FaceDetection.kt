package com.baghdad.islamic_image_loader.model
import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

internal fun detectAndCropFace(inputBitmap: Bitmap, onFaceCropped: (Bitmap?) -> Unit) {
    val image = InputImage.fromBitmap(inputBitmap, 0)

    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .build()

    val detector = FaceDetection.getClient(options)

    detector.process(image)
        .addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                val face: Face = faces[0]
                val bounds: Rect = face.boundingBox

                val safeBounds = Rect(
                    bounds.left.coerceAtLeast(0),
                    bounds.top.coerceAtLeast(0),
                    bounds.right.coerceAtMost(inputBitmap.width),
                    bounds.bottom.coerceAtMost(inputBitmap.height)
                )

                val croppedFace = Bitmap.createBitmap(
                    inputBitmap,
                    safeBounds.left,
                    safeBounds.top,
                    safeBounds.width(),
                    safeBounds.height()
                )
                onFaceCropped(croppedFace)
            } else {
                onFaceCropped(null)
            }
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            onFaceCropped(null)
        }
}