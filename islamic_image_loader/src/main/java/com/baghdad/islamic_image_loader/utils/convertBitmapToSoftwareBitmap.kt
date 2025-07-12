package com.baghdad.islamic_image_loader.utils

import android.graphics.Bitmap

fun convertBitmapToSoftwareBitmap(bitmap: Bitmap): Bitmap {
    return if (bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else bitmap
}