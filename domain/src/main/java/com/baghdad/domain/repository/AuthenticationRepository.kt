package com.baghdad.domain.repository

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String): String
    suspend fun isUserLoggedIn(): Boolean
}