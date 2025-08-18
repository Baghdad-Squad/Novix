package com.baghdad.domain.repository

import com.baghdad.entity.user.User

interface AuthenticationRepository {
    suspend fun login(userName: String, password: String)
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUserInfo(): User?
    suspend fun logOut(): Boolean
}