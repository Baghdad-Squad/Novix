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
        val invocation = chain.request().tag(Invocation::class.java)
            ?: return chain.proceed(chain.request())
        val shouldAttachAuthHeader = invocation
            .method()
            .annotations
            .any { it.annotationClass == Authenticated::class }
        val language = languageProvider.getCurrentLanguage()

        val modifiedRequest = originalRequest.newBuilder().apply {
            if (shouldAttachAuthHeader) {
                addHeader(authorizationHeaderKey, "$authorizationHeaderPrefix$authorizationToken")
            }

            addHeader(acceptHeaderKey, acceptHeaderValue)

            url(
                originalRequest.url.newBuilder()
                    .addQueryParameter(languageQueryKey, language)
                    .build()
            )
        }.build()

        return chain.proceed(modifiedRequest)
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated