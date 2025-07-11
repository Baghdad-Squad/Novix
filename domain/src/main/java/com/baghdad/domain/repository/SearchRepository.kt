package com.baghdad.domain.repository

import com.baghdad.domain.model.search.SearchResult
import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchByName(query : String) : SearchResult
    suspend fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun deleteRecentSearchById(id : Long)
    suspend fun deleteAllRecentSearches()
}