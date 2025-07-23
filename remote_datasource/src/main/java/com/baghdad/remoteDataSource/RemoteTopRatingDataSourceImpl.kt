package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.movie.toPagedMovieDtos
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteTopRatingDataSource
import com.baghdad.repository.logger.Logger
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
    ): PagedResultDto<MovieDto> {
        val endpoint = TOP_RATED_MOVIES_ENDPOINT

        val response = handleRequest<SimilarMovieResponse>(
            client = httpClient,
            logger = logger,
            url = "$baseUrl$endpoint",
            params = mapOf("page" to page.toString())
        )
        return response.toPagedMovieDtos()
    }
    companion object {
        private const val TOP_RATED_MOVIES_ENDPOINT = "/movie/top_rated"
    }
}