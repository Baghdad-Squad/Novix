package com.baghdad.remoteDataSource.interceptor


import com.baghdad.repository.language.LanguageProvider
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class HeadersSetupInterceptor(
    private val languageProvider: LanguageProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation =
            chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())
        val shouldAttachAuthHeader =
            invocation.method().annotations.any { it.annotationClass == Authenticated::class }
        val myUrl = chain.request().url.newBuilder().apply {
            addQueryParameter("api_key", "85a353d1b5dbabfae4dbe5f6fc31d125")
        }.build()


        return chain.proceed(chain.request().newBuilder().apply {
            if (shouldAttachAuthHeader) {
                // should replace this static token with saveToken logic
//                        if (userLocalDataSource.getUserToken().isNotEmpty()) {
                addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NWEzNTNkMWI1ZGJhYmZhZTRkYmU1ZjZmYzMxZDEyNSIsIm5iZiI6MTc1MTU3NzIwMy40NTYsInN1YiI6IjY4NjZmMjczYzg0OTU0ZjcwMWZjYmJjMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.pMdn4G9Kqv4UXKy4y_cPOtZx9XqwodQ7q8JxEvTM4VA"
                    //"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyZTZkYmRkOTNjMGY5NzdkMjMwOGJjMzM3NmI3YTNmOCIsIm5iZiI6MTc1MzAyOTE3Ni45OSwic3ViIjoiNjg3ZDFhMzgzOTg0OWZmZThkZDk4ZDEzIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.5NDfRH9_oRVtrvQb8Bs11qWGeLzEE5US_e5IcVQWerE"
                )
//                        }
            }
            addHeader("Accept", "application/json")
            addHeader("language", languageProvider.getCurrentLanguage())
        }
            .url(myUrl)
            .build())
    }
}

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated