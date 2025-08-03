package com.baghdad.domain.repository

interface SavedListRepository {
    suspend fun createSavedList(title: String)
}
