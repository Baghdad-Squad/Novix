package com.baghdad.repository.datasource.local

interface LocalSessionDataSource {
    suspend fun saveSessionId(sessionId: String)
    suspend fun getSessionId(): String?
    suspend fun deleteSessionId()
}