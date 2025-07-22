package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.LoginDto

interface LocalLoginDataSource {
    suspend fun addLogin(login: LoginDto)
    suspend fun getLoginByUserId(userId: Long): LoginDto
    suspend fun getLoginBySessionId(sessionId: String): LoginDto
    suspend fun getLoginByUsernameAndPassword(username: String, password: String): LoginDto
    suspend fun saveLogin(login: LoginDto)
    suspend fun deleteLoginByUserId(userId: Long)

}