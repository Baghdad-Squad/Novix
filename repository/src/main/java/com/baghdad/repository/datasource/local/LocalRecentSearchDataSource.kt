package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.RecentSearchDto
import kotlinx.coroutines.flow.Flow

interface LocalRecentSearchDataSource {
    suspend fun deleteAllRecentSearches()

    suspend fun deleteRecentSearchById(id: Long)

    suspend fun addRecentSearchQuery(query: String)

    fun getAllRecentSearches(): Flow<List<RecentSearchDto>>
}