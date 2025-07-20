package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.interceptor.KtorApiInterceptor
import com.baghdad.repository.language.LanguageProvider
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


class KtorApiInterceptorTest {
    @Test
    fun `ApiInterceptor should adds api_key parameter to requests`() = runTest {
        val fakeApiKey = "TEST_KEY"
        val fakeLanguage = "ar"

        val mockLanguageProvider = object : LanguageProvider {
            override fun getCurrentLanguage(): String = fakeLanguage
        }

        var capturedUrl: String? = null

        val client = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(Json)
            }
            install(KtorApiInterceptor) {
                apiKey = fakeApiKey
                languageProvider = mockLanguageProvider
            }
            engine {
                addHandler { request ->
                    capturedUrl = request.url.toString()
                    respondOk()
                }
            }
        }

        client.get("https://fake.api.com/test")

        assertTrue(capturedUrl!!.contains("api_key=$fakeApiKey"))
        assertTrue(capturedUrl!!.contains("language=$fakeLanguage"))
    }
}