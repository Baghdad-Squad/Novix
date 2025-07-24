package com.baghdad.repository.datasource.local

interface LocalSessionDataStore {
    suspend fun saveSessionId(sessionId: String)
    suspend fun getSessionId(): String?
    suspend fun deleteSessionId()
}