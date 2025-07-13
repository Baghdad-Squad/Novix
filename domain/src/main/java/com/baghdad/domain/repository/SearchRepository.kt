package com.baghdad.domain.repository

import com.baghdad.domain.model.search.RecentSearch
import com.baghdad.domain.model.search.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchByName(query : String) : SearchResult
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun deleteRecentSearchById(id : Long)
    suspend fun deleteAllRecentSearches()
}