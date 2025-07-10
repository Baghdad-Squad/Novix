package com.baghdad.remote_datasource.api

import com.baghdad.remote_datasource.entity.SearchParameter
import com.baghdad.remote_datasource.entity.SearchResponse
import com.baghdad.remote_datasource.entity.toParams
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbApi(
    private val httpClient: HttpClient,
    private val apiKey: String,
    private val url : String
) {
    suspend fun searchMulti(params: SearchParameter): SearchResponse {
        return httpClient.get(url) {
            parameter("api_key", apiKey)
            params.toParams().forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }
}