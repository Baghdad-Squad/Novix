package com.baghdad.islamic_image_loader.ui

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.request.crossfade

object CustomImageLoader {
    private var imageLoader: ImageLoader? = null
    fun init(context: Context) {

        imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(20L * 1024 * 1024)
                    .build()
            }
            .build()
    }

    fun get(): ImageLoader? = imageLoader

}
