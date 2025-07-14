package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.util.interceptor.ApiInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test



class ApiInterceptorTest {
    @Test
    fun `ApiInterceptor should adds api_key parameter to requests`() = runTest {
        val testApiKey = "test_key_123"
        var apiKeyFound = false

        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            apiKeyFound = url.contains("api_key=$testApiKey")
            respondOk("{}")
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(ApiInterceptor) {
                apiKey = testApiKey
            }
        }

        client.get("https://example.com/test")
        assertTrue(apiKeyFound, "api_key parameter should be present in the request URL")
    }
}