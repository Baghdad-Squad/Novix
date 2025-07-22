package com.baghdad.domain.repository

import com.baghdad.entity.login.Login

interface LoginRepository {
    suspend fun getLoginByUserId(userId: Long): Login
    suspend fun getLoginBySessionId(sessionId: Int): Login
    suspend fun getLoginByUsernameAndPassword(username: String, password: String): Login
}