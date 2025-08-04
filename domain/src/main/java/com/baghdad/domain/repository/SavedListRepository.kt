package com.baghdad.domain.repository

interface SavedListRepository {
    suspend fun deleteSavedListByTitle(title: String)
}
