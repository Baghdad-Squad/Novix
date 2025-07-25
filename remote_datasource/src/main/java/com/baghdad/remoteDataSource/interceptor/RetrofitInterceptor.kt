package com.baghdad.remoteDataSource.interceptor


import com.baghdad.repository.language.LanguageProvider
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class HeadersSetupInterceptor(
    private val languageProvider: LanguageProvider,
    private val authorizationToken: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation =
            chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())
        val shouldAttachAuthHeader =
            invocation.method().annotations.any { it.annotationClass == Authenticated::class }

        return chain.proceed(chain.request().newBuilder().apply {
            if (shouldAttachAuthHeader) {
                addHeader(
                    "Authorization",
                    "Bearer $authorizationToken"
                )
            }
            addHeader("Accept", "application/json")
            addHeader("language", languageProvider.getCurrentLanguage())
        }.build())
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated