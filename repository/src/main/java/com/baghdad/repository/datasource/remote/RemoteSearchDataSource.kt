package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.SearchResultDto

interface RemoteSearchDataSource {
    suspend fun searchMultiMedia(
        query: String,
        pageNumber: Int,
        movieGenres: List<GenreDto>? = null,
        tvGenres: List<GenreDto>? = null
    ): SearchResultDto
}
