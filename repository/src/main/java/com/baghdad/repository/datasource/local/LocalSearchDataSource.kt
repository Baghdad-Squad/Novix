package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.RecentSearchDto

interface LocalSearchDataSource {
    suspend fun addRecentSearchQuery(query: String)
    suspend fun getAllRecentSearches() : List<RecentSearchDto>
    suspend fun deleteRecentSearchById(id : Long)
    suspend fun deleteAllRecentSearches()
}