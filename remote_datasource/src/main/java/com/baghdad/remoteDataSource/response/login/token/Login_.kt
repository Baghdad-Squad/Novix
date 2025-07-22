package com.baghdad.remoteDataSource.response.login.token

import com.baghdad.remoteDataSource.response.login.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


class login_(private val apiKey: String) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun createSession(requestToken: String): String? {
        return try {
            val response: LoginResponse = client.post(BASE_URL) {
                parameter(API_KEY, apiKey)
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToJsonElement(mapOf(REQUEST_TOKEN to requestToken)))
            }.body()

            if (response.success == true) {
                response.sessionId
            } else {
               // Exception
                null
            }

        } catch (e: Exception) {
           //Exception
            null
        }
    }
    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/authentication/session/new"
        const val API_KEY = "api_key"
        const val REQUEST_TOKEN = "request_token"
    }
}

