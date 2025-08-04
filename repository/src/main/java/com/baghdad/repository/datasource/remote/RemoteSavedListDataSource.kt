package com.baghdad.repository.datasource.remote

interface RemoteSavedListDataSource {
    suspend fun deleteSavedListByTitle(title: String)
}
