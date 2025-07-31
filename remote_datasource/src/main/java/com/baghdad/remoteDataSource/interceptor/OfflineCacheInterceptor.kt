package com.baghdad.remoteDataSource.interceptor

import com.baghdad.remoteDataSource.util.Connectivity
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class OfflineCacheInterceptor(private val connectivity: Connectivity) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        if (!connectivity.isConnected()) {
            requestBuilder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(requestBuilder.build())
    }
}