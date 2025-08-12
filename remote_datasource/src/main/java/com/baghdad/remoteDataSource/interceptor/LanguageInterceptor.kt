package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.datasource.local.AppConfigurationDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class LanguageInterceptor(
    private val appConfigurationDataSource: AppConfigurationDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val invocation = originalRequest.tag(Invocation::class.java)

        val shouldForceEnglishLocale = invocation
            ?.method()
            ?.annotations
            ?.any { it.annotationClass == ForceLocaleEnglish::class } == true

        val language = if (shouldForceEnglishLocale) {
            "en"
        } else {
            runBlocking { appConfigurationDataSource.getAppLanguage().first() }
        }

        val modifiedRequest = originalRequest.newBuilder().apply {
            url(
                originalRequest.url.newBuilder().addQueryParameter(languageQueryKey, language)
                    .build()
            )
        }.build()

        return chain.proceed(modifiedRequest)
    }

    companion object {
        private const val languageQueryKey = "language"
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class ForceLocaleEnglish