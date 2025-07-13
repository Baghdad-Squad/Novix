package com.baghdad.remote_datasource

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.util.*

class ApiInterceptor(private val apiKey: String) {
    class Config {
        var apiKey: String = ""
    }
    
    companion object Plugin : HttpClientPlugin<Config, ApiInterceptor> {
        override val key: AttributeKey<ApiInterceptor> = AttributeKey("ApiKeyInterceptor")
        
        override fun prepare(block: Config.() -> Unit): ApiInterceptor {
            val config = Config().apply(block)
            return ApiInterceptor(config.apiKey)
        }
        
        override fun install(plugin: ApiInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                context.parameter("api_key", plugin.apiKey)
            }
        }
    }
}