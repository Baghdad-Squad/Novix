package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.GenreApiService
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.response.GenreListResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteGenreDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto

class RemoteGenreDataSourceImpl(
    private val genreApiService: GenreApiService,
    private val logger: Logger,
): RemoteGenreDataSource {
    override suspend fun getMovieGenre(language: String): List<GenreDto> {
        return handleRequest<GenreListResponse>(
            apiCall = {genreApiService.getMovieGenre(language)},
            logger = logger,
        ).toDto(genreType = GenreDto.GenreType.MOVIE)
    }

    override suspend fun getTvShowGenre(language: String): List<GenreDto> {
        return handleRequest<GenreListResponse>(
            apiCall = {genreApiService.getTvShowGenre(language)},
            logger = logger,
        ).toDto(genreType = GenreDto.GenreType.TV_SHOW)
    }
}