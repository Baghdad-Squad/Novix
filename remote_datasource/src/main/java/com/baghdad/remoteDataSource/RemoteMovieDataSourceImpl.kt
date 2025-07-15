package com.baghdad.remoteDataSource

import MovieDetailsResponse
import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.response.CreditsResponse
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MovieDto
import io.ktor.client.HttpClient

class RemoteMovieDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
): RemoteMovieDataSource {
    override suspend fun getSimilarMovies(movieId: Long): List<MovieDto> {
        val endpoint = SIMILAR_MOVIES_ENDPOINT.replace("{movie_id}", movieId.toString())
        return handleRequest<SimilarMovieResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).results?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getMovieDetails(movieId: Long): MovieDto {
        val endpoint = MOVIE_DETAILS_ENDPOINT.replace("{movie_id}", movieId.toString())
        return handleRequest<MovieDetailsResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).toDto()
    }

    override suspend fun getMovieCredits(movieId: Long): List<CastMemberDto> {
        val endpoint = MOVIE_CREDITS_ENDPOINT.replace("{movie_id}", movieId.toString())
        return handleRequest<CreditsResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    companion object {
        private const val SIMILAR_MOVIES_ENDPOINT = "/movie/{movie_id}/similar"
        private const val MOVIE_DETAILS_ENDPOINT = "/movie/{movie_id}"
        private const val MOVIE_CREDITS_ENDPOINT = "/movie/{movie_id}/credits"
    }
}
