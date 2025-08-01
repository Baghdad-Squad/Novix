package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.language.LanguageProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Invocation
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

class HeadersSetupInterceptorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var languageProvider: LanguageProvider
    private lateinit var interceptor: HeadersSetupInterceptor

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        languageProvider = mockk(relaxed = true)
        every { languageProvider.getCurrentLanguage() } returns LANGUAGE
        interceptor = HeadersSetupInterceptor(languageProvider, AUTHORIZATION_TOKEN)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should add Authorization header when request is authenticated`() = runTest {
        // Given
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .tag(Invocation::class.java, createMockInvocation(true))
            .build()

        // When
        val response = interceptor.intercept(createMockChain(request))

        // Then
        assertThat(response.request.header("Authorization"))
            .isEqualTo("Bearer $AUTHORIZATION_TOKEN")
    }

    @Test
    fun `should not add Authorization header when request is not authenticated`() = runTest {
        // Given
        mockWebServer.enqueue(MockResponse().setResponseCode(200))
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .tag(Invocation::class.java, createMockInvocation(false))
            .build()
        // When
        val response = interceptor.intercept(createMockChain(request))

        // Then
        assertThat(response.request.header("Authorization")).isNull()
    }

    private fun createMockChain(originalRequest: Request): Interceptor.Chain =
        object : Interceptor.Chain {
            override fun request(): Request = originalRequest

            override fun proceed(request: Request): Response = Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("".toResponseBody(null))
                .build()

            override fun connection(): Connection? = null
            override fun call(): Call = mockk(relaxed = true)
            override fun connectTimeoutMillis(): Int = 0
            override fun readTimeoutMillis(): Int = 0
            override fun writeTimeoutMillis(): Int = 0
            override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
            override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
            override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
        }

    private fun createMockInvocation(hasAuthenticated: Boolean): Invocation {
        val method = mockk<Method>().apply {
            every { annotations } returns if (hasAuthenticated) arrayOf(Authenticated()) else emptyArray()
        }
        return mockk<Invocation>().apply {
            every { method() } returns method
            every { arguments() } returns emptyList()
        }
    }

    companion object {
        private const val AUTHORIZATION_TOKEN = "test_token"
        private val LANGUAGE = "en"
    }

}