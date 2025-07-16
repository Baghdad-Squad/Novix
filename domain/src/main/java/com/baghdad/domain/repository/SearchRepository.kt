package com.baghdad.domain.repository

import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun deleteRecentSearchById(id : Long)
    suspend fun deleteAllRecentSearches()
}