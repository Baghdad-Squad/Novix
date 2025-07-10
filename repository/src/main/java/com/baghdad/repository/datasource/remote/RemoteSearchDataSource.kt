package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.SearchResultDto

interface RemoteSearchDataSource {
    suspend fun searchMultiResults(
        query: String,
        pageNumber: Int,
    ): SearchResultDto
}
