package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

interface RemoteTopRatingDataSource {
    suspend fun getTopRatedMovies(page: Int,genres: List<GenreDto>): PagedResultDto<MovieDto>
}