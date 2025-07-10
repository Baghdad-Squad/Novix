package com.baghdad.domain.repository

import com.baghdad.domain.result.SearchResult
import com.baghdad.entity.search.RecentSearch

interface SearchRepository {
    suspend fun searchByName(query : String) : SearchResult
    suspend fun getRecentSearches() : List<RecentSearch>
    suspend fun deleteRecentSearchById(id : Long)
    suspend fun deleteAllRecentSearches()
}