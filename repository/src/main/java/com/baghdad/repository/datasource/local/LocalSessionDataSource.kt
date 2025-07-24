package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.UserDto

interface LocalSessionDataSource {
    suspend fun saveSessionId(sessionId: String)

    suspend fun getSessionId(): String?
    suspend fun getUserDetails():UserDto?
    suspend fun clearSession()
    suspend fun deleteSessionId()
}