package com.baghdad.repository

import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SearchRepositoryImpl : SearchRepository   {
    override suspend fun searchByName(query: String): SearchResult {
        // TODO("Not yet implemented")
        return SearchResult(
            actors = emptyList(),
            movies = emptyList(),
            tvShows = emptyList()
        )
    }

    override suspend fun getRecentSearches(): Flow<List<RecentSearch>> {
        // TODO("Not yet implemented")
        return flowOf(emptyList())
    }

    override suspend fun deleteRecentSearchById(id: Long) {
       // TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecentSearches() {
       // TODO("Not yet implemented")
    }
}