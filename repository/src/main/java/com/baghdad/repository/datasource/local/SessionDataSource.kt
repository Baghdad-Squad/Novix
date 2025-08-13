package com.baghdad.repository.datasource.local

interface SessionDataSource {
    suspend fun saveSessionId(sessionId: String)
    suspend fun getSessionId(): String?
    suspend fun deleteSessionId()
}