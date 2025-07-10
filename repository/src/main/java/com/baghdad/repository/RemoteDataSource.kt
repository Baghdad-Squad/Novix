package com.baghdad.repository

import com.baghdad.repository.model.SearchResultDto


interface RemoteDataSource {
    suspend fun searchMultiResults(
        query: String,
        pageNumber: Int,
    ): SearchResultDto
}