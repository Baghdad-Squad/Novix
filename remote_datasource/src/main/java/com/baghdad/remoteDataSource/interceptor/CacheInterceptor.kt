package com.baghdad.remoteDataSource.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder().header(
            "Cache-Control", "public, max-age=86400, must_revalidate"
        ).build()
    }
}