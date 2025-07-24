package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.SearchApiService
import com.baghdad.remoteDataSource.mapper.search.toPagedActorDtos
import com.baghdad.remoteDataSource.mapper.search.toPagedMovieDtos
import com.baghdad.remoteDataSource.mapper.search.toPagedTvShowDtos
import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.response.search.TvShowSearchResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteSearchDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.TvShowDto

class RemoteSearchDataSourceImpl(
    private val searchApiService: SearchApiService,
    private val logger: Logger,
) : RemoteSearchDataSource {
    override suspend fun searchMovies(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<MovieDto> {
        val searchResponse = handleRequest<MovieSearchResponse>(
            logger = logger,
            apiCall = {
                searchApiService.searchMovies(
                    query = query,
                    page = page,
                )
            },
        )
        return searchResponse.toPagedMovieDtos(genres = genres)
    }

    override suspend fun getMoviesResultCount(title: String): Int {
        val searchResponse = handleRequest<MovieSearchResponse>(
            apiCall = { searchApiService.getMoviesResultCount(title) },
            logger = logger,
        )
        return searchResponse.totalResults ?: 0
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<TvShowDto> {
        val searchResponse = handleRequest<TvShowSearchResponse>(
            apiCall = {
                searchApiService.searchTvShows(
                    query = query,
                    page = page
                )
            },
            logger = logger,
        )
        return searchResponse.toPagedTvShowDtos(genres = genres)
    }

    override suspend fun getTvShowsResultCount(title: String): Int {
        val searchResponse = handleRequest<TvShowSearchResponse>(
            apiCall = { searchApiService.getTvShowsResultCount(title) },
            logger = logger,
        )
        return searchResponse.totalResults ?: 0
    }

    override suspend fun searchActors(
        query: String,
        page: Int,
    ): PagedResultDto<ActorDto> {
        val searchResponse = handleRequest<ActorSearchResponse>(
            apiCall = {
                searchApiService.searchActors(
                    query = query,
                    page = page
                )
            },
            logger = logger,
        )
        return searchResponse.toPagedActorDtos()
    }

    override suspend fun getActorsResultCount(name: String): Int {
        val searchResponse = handleRequest<ActorSearchResponse>(
            apiCall = { searchApiService.getActorsResultCount(name) },
            logger = logger,
        )
        return searchResponse.totalResults ?: 0
    }
}