package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.language.LanguageProvider
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class HeadersSetupInterceptor @Inject constructor(
    private val languageProvider: LanguageProvider,
    private val authorizationToken: String
) : Interceptor {

    private val acceptHeaderKey = "Accept"
    private val acceptHeaderValue = "application/json"
    private val authorizationHeaderKey = "Authorization"
    private val authorizationHeaderPrefix = "Bearer "
    private val languageQueryKey = "language"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val invocation = originalRequest.tag(Invocation::class.java)
        val shouldAttachAuthHeader = invocation
                ?.method()
                ?.annotations
                ?.any { it.annotationClass == Authenticated::class } == true

        val shouldForceEnglishLocale = invocation
            ?.method()
            ?.annotations
            ?.any { it.annotationClass == ForceLocaleEnglish::class } == true

        val language = if (shouldForceEnglishLocale) {
            "en"
        } else {
            languageProvider.getCurrentLanguage()
        }

        val modifiedRequest = originalRequest.newBuilder().apply {
            if (shouldAttachAuthHeader) {
                addHeader(authorizationHeaderKey, "$authorizationHeaderPrefix$authorizationToken")
            }

            addHeader(acceptHeaderKey, acceptHeaderValue)

            url(
                originalRequest.url.newBuilder().addQueryParameter(languageQueryKey, language)
                    .build()
            )
        }.build()

        return chain.proceed(modifiedRequest)
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated

@Target(AnnotationTarget.FUNCTION)
annotation class ForceLocaleEnglish