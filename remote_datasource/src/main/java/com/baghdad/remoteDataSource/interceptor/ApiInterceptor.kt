package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.language.LanguageProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.parameter
import io.ktor.util.AttributeKey

class ApiInterceptor(
    private val apiKey: String,
    private val languageProvider: LanguageProvider,
) {

    companion object Plugin : HttpClientPlugin<Config, ApiInterceptor> {

        override val key: AttributeKey<ApiInterceptor> = AttributeKey("ApiKeyInterceptor")

        override fun prepare(block: Config.() -> Unit): ApiInterceptor {
            val config = Config().apply(block)
            return ApiInterceptor(config.apiKey, config.languageProvider)
        }

        override fun install(plugin: ApiInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Phases.State) {
                context.parameter("api_key", plugin.apiKey)
                context.parameter("language", plugin.languageProvider.getCurrentLanguage())
            }
        }
    }
}