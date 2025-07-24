package com.baghdad.domain.repository

import com.baghdad.entity.User

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String): String
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getLoggedInUser(): User?
    suspend fun logOut(): Boolean
}