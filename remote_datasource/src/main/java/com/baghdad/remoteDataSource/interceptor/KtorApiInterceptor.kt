package com.baghdad.remoteDataSource.interceptor

import com.baghdad.repository.language.LanguageProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.parameter
import io.ktor.util.AttributeKey

class KtorApiInterceptor(
    private val apiKey: String,
    private val languageProvider: LanguageProvider,
) {

    companion object Plugin : HttpClientPlugin<Config, KtorApiInterceptor> {

        override val key: AttributeKey<KtorApiInterceptor> = AttributeKey("ApiKeyInterceptor")

        override fun prepare(block: Config.() -> Unit): KtorApiInterceptor {
            val config = Config().apply(block)
            return KtorApiInterceptor(config.apiKey, config.languageProvider)
        }

        override fun install(plugin: KtorApiInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Phases.State) {
                context.parameter("api_key", plugin.apiKey)
                context.parameter("language", plugin.languageProvider.getCurrentLanguage())
            }
        }
    }
}