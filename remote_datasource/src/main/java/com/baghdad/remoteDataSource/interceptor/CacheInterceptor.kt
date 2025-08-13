package com.baghdad.remoteDataSource.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val invocation = chain.request().tag(Invocation::class.java)

        if (chain.request().method != GET_REQUEST || !response.isSuccessful) {
            return response
        }

        val hasNoCache =
            invocation
                ?.method()
                ?.annotations
                ?.none { it.annotationClass == Cacheable::class } == true

        if (hasNoCache) return response

        return response
            .newBuilder()
            .header(
            CACHE_CONTROL_HEADER,
                CACHE_CONTROL_VALUE,
            ).build()
    }

    companion object {
        private const val CACHE_CONTROL_HEADER = "Cache-Control"
        private const val CACHE_CONTROL_VALUE = "public, max-age=86400, must-revalidate"
        private const val GET_REQUEST = "GET"
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cacheable