package com.baghdad.islamic_image_loader.ui

import android.content.Context
import android.util.Log
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.File

object CustomImageLoader {
    private var imageLoader: ImageLoader? = null
    fun init(context: Context) {
        val cacheDir = File(context.cacheDir, "images_cache")
        val cacheSize = 20L * 1024 * 1024

        val myOkHttpClient = OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize))
            .addNetworkInterceptor(CacheInterceptor())
            .build()
        imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = {
                            myOkHttpClient
                        },
                    )
                )
            }
//            .diskCache {
//                DiskCache.Builder()
//                    .directory(context.cacheDir.resolve("image_cache"))
//                    .maxSizePercent(0.05)
//                    .
//                    .build()
//            }
            .build()
    }

    fun get(): ImageLoader? = imageLoader

}

class CacheInterceptor(
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        println("🚨 CacheInterceptor INTERCEPTED")
        val request = chain.request().newBuilder()
            .addHeader("Cache-Control", "public, max-age=86400")
            .build()
        val response = chain.proceed(request)
        if (response.cacheResponse != null) {
            Log.d("CacheInterceptor", "Loaded from cache")
        } else if (response.networkResponse != null) {
            Log.d("CacheInterceptor", "Loaded from network")
        } else {
            Log.i("CacheInterceptor", "Loaded from nowhere image interceptor code ${response.code}")
        }
        return response
    }
}