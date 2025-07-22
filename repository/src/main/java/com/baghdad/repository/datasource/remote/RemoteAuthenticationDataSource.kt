package com.baghdad.repository.datasource.remote

interface RemoteAuthenticationDataSource {
    suspend fun getRequestToken(): String
    suspend fun validateCredentialWithToken(
        userName: String,
        password: String,
        requestToken: String
    ): String

    suspend fun createSession(requestToken: String): String
}