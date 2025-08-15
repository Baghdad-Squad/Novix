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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteSearchDataSourceImpl @Inject constructor(
    private val searchApiService: SearchApiService,
    private val logger: Logger,
) : RemoteSearchDataSource {
    override suspend fun searchMovies(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<MovieDto> {
       return handleRequest<MovieSearchResponse>(
            logger = logger,
            apiCall = {
                searchApiService.searchMovies(
                    query = query,
                    page = page,
                )
            },
        ).toPagedMovieDtos(genres = genres)
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<TvShowDto> {
        return handleRequest<TvShowSearchResponse>(
            apiCall = {
                searchApiService.searchTvShows(
                    query = query,
                    page = page
                )
            },
            logger = logger,
        ).toPagedTvShowDtos(genres = genres)
    }

    override suspend fun getMoviesResultCount(title: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowsResultCount(title: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getActorsResultCount(name: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun searchActors(
        query: String,
        page: Int,
    ): PagedResultDto<ActorDto> {
        return handleRequest<ActorSearchResponse>(
            apiCall = {
                searchApiService.searchActors(
                    query = query,
                    page = page
                )
            },
            logger = logger,
        ).toPagedActorDtos()
    }
}