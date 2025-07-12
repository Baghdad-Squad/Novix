package com.baghdad.islamic_image_loader.utils

import android.content.Context
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
    val assetFileDescriptor = context.assets.openFd(modelPath)
    FileInputStream(assetFileDescriptor.fileDescriptor).use { inputStream ->
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}