package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.SearchQueryDto

interface LocalSearchQueryDataSource {
    suspend fun addSearchQuery(searchQuery: SearchQueryDto)
    suspend fun addSearchQueries(queries: List<SearchQueryDto>)
    suspend fun getInvalidSearchQueries(timestamp: Long): List<SearchQueryDto>
    suspend fun getAllSearchQueries(): List<SearchQueryDto>
    suspend fun getSearchByQuery(
        query: String,
        type: SearchQueryDto.MediaType
    ): List<SearchQueryDto>
    suspend fun deleteInvalidSearchQueries(timestamp: Long)
    suspend fun deleteAllSearchQueries()
}
