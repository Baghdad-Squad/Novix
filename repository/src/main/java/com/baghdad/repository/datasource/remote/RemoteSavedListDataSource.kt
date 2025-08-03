package com.baghdad.repository.datasource.remote

interface RemoteSavedListDataSource {
    suspend fun createSavedList(title: String, sessionId: String)
}
