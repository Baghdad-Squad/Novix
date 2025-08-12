package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.exception.UnauthorizedNetworkException
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AuthenticationInterceptor
    @Inject
    constructor(
        private val authorizationToken: String,
        private val sessionDataSource: LocalSessionDataSource,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val invocation = originalRequest.tag(Invocation::class.java)
            val shouldAttachAuthHeader =
                invocation
                    ?.method()
                    ?.annotations
                    ?.any { it.annotationClass == Authenticated::class } == true

            val requiresSession =
                invocation
                    ?.method()
                    ?.annotations
                    ?.any { it.annotationClass == RequiresSession::class } == true

            if (requiresSession) {
                val sessionId = runBlocking { sessionDataSource.getSessionId() }
                if (sessionId == null) {
                    throw UnauthorizedNetworkException()
                }
            }

            val modifiedRequest =
                originalRequest
                    .newBuilder()
                    .apply {
                        if (shouldAttachAuthHeader) {
                            addHeader(
                                AUTHORIZATION_HEADER_KEY,
                                "$AUTHORIZATION_HEADER_PREFIX$authorizationToken",
                            )
                        }

                        addHeader(ACCEPT_HEADER_KEY, ACCEPT_HEADER_VALUE)

                        if (requiresSession) {
                            val sessionId = runBlocking { sessionDataSource.getSessionId() }
                            url(
                                originalRequest.url
                                    .newBuilder()
                                    .addQueryParameter(SESSION_ID_QUERY_KEY, sessionId)
                                    .build(),
                            )
                        }
                    }.build()

            return chain.proceed(modifiedRequest)
        }

        companion object {
            private const val ACCEPT_HEADER_KEY = "Accept"
            private const val ACCEPT_HEADER_VALUE = "application/json"
            private const val AUTHORIZATION_HEADER_KEY = "Authorization"
            private const val AUTHORIZATION_HEADER_PREFIX = "Bearer "
            private const val SESSION_ID_QUERY_KEY = "session_id"
        }
    }

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated

@Target(AnnotationTarget.FUNCTION)
annotation class RequiresSession
