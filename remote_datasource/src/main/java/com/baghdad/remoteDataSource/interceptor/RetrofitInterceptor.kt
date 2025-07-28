package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.language.LanguageProvider
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class HeadersSetupInterceptor(
    private val languageProvider: LanguageProvider,
    private val authorizationToken: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation =
            chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())
        val shouldAttachAuthHeader =
            invocation.method().annotations.any { it.annotationClass == Authenticated::class }
        val language = languageProvider.getCurrentLanguage()

        return chain.proceed(
            chain
                .request()
                .newBuilder()
                .apply {
                    if (shouldAttachAuthHeader) {
                        addHeader(
                            "Authorization",
                            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZTZkYmRkOTNjMGY5NzdkMjMwOGJjMzM3NmI3YTNmOCIsIm5iZiI6MTc1MzAyOTE3Ni45OSwic3ViIjoiNjg3ZDFhMzgzOTg0OWZmZThkZDk4ZDEzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.5NDfRH9_oRVtrvQb8Bs11qWGeLzEE5US_e5IcVQWerE",
                        )
                    }
                    addHeader("Accept", "application/json")
                    url(
                        chain
                            .request()
                            .url
                            .newBuilder()
                            .addQueryParameter("language", language)
                            .build(),
                    )
                }.build(),
        )
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated
