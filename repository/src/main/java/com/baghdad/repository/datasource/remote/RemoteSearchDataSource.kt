package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto
import com.baghdad.repository.model.actor.ActorDto

interface RemoteSearchDataSource {
    suspend fun searchMovies(
        query: String,
        page: Int?,
        genres: List<GenreDto>
    ): PagedResultDto<MovieDto>

    suspend fun searchTvShows(
        query: String,
        page: Int?,
        genres: List<GenreDto>
    ): PagedResultDto<TvShowDto>

    suspend fun searchActors(
        query: String,
        page: Int?
    ): PagedResultDto<ActorDto>
}