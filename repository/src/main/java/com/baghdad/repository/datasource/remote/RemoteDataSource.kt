package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.SearchResultDto

interface RemoteDataSource {
    suspend fun searchMultiResults(
        query: String,
        pageNumber: Int,
    ): SearchResultDto

    suspend fun getMovieGenre(
        language: String
    ): List<GenreDto>

    suspend fun getTvShowGenre(
        language: String
    ): List<GenreDto>


}
