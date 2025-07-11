package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.RecentSearchDto
import kotlinx.coroutines.flow.Flow

interface LocalSearchDataSource {
    suspend fun addRecentSearchQuery(query: String)
    suspend fun getAllRecentSearches(): Flow<List<RecentSearchDto>>
    suspend fun deleteRecentSearchById(id: Long)
    suspend fun deleteAllRecentSearches()
}