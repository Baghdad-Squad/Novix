package com.baghdad.islamic_image_loader.model

import android.content.Context
import coil3.Image
import coil3.ImageLoader
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import okhttp3.OkHttpClient

internal suspend fun ImageUrlLoader(imageUrl: String, context: Context): Image? {
    val imageLoader = buildImageLoader(context)
    val headers = buildNetworkHeaders()
    val request = buildImageRequest(imageUrl, context, headers)
    val imageResult = imageLoader.execute(request)

    return when (imageResult) {
        is SuccessResult -> imageResult.image
        is ErrorResult -> {
            null
        }
    }
}

private fun buildImageRequest(
    imageUrl: String,
    context: Context,
    headers: NetworkHeaders
): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .httpHeaders(headers)
        .build()
}

private fun buildImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context).components {
        add(
            OkHttpNetworkFetcherFactory(
                callFactory = { OkHttpClient() }
            )
        )
    }.build()
}

private fun buildNetworkHeaders(): NetworkHeaders {
    return NetworkHeaders.Builder()
        .set("Cache-Control", "no-cache")
        .set("Accept", "image/png,image/jpeg,image/webp,image/*,*/*")
        .build()
}
