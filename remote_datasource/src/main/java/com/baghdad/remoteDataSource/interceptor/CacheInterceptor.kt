package com.baghdad.remoteDataSource.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val noCachePaths = listOf<String>("list", "account", "authentication")
        if (noCachePaths.any { chain.request().url.encodedPath.contains(it) }) {
            return response
        }
        if (chain.request().method != "GET") return response
        if (!response.isSuccessful) {
            return response
        }
        return response.newBuilder().header(
            "Cache-Control", "public, max-age=86400, must_revalidate"
        ).build()
    }
}