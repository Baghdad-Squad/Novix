package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

interface RemoteSearchDataSource {
    suspend fun getMoviesResultCount(title: String): Int

    suspend fun getTvShowsResultCount(title: String): Int

    suspend fun getActorsResultCount(name: String): Int

    suspend fun searchActors(query: String, page: Int): PagedResultDto<ActorDto>

    suspend fun searchMovies(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<MovieDto>

    suspend fun getMoviesResultCount(title: String): Int

    suspend fun searchTvShows(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<TvShowDto>
}