package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto

interface LocalTrendingTvShowsDataSource {
    suspend fun getTvShowsGenres(language: String): List<GenreDto>
    suspend fun getTrendingTvShows(page: Int, pageSize: Int): List<TvShowDto>
    suspend fun saveTrendingTvShows(tvShows: List<TvShowDto>)
}