package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.search.toPagedMovieDtos
import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteTopRatingDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import io.ktor.client.HttpClient

class RemoteTopRatingDataSourceImpl(
    private val httpClient: HttpClient,
    private val logger: Logger,
    private val baseUrl: String
) : RemoteTopRatingDataSource {

    override suspend fun getTopRatedMovies(
        page: Int,
        genres: List<GenreDto>
    ): PagedResultDto<MovieDto> {
        val endpoint = TOP_RATED_MOVIES_ENDPOINT

        val response = handleRequest<MovieSearchResponse>(
            client = httpClient,
            logger = logger,
            url = "$baseUrl$endpoint",
            params = mapOf("page" to page.toString())
        )
        return response.toPagedMovieDtos(genres = genres)
    }

    companion object {
        private const val TOP_RATED_MOVIES_ENDPOINT = "/movie/top_rated"
    }
}