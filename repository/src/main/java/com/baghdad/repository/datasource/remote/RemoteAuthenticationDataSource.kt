package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.UserDto

interface RemoteAuthenticationDataSource {
    suspend fun getRequestToken(): String
    suspend fun validateCredentialWithToken(
        userName: String,
        password: String,
        requestToken: String
    ): String

    suspend fun createSession(requestToken: String): String

    suspend fun getUserDetails(sessionId: String): UserDto

    suspend fun deleteSession(sessionId: String): Boolean
}