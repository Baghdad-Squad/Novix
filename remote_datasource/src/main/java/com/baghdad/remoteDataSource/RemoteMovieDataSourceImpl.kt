package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.movie.toDto
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteMovieDataSource
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.ReviewDto
import io.ktor.client.HttpClient

class RemoteMovieDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : RemoteMovieDataSource {
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

    override suspend fun getMovieCastMembers(movieId: Long): List<CastMemberDto> {
        val endpoint = MOVIE_CREDITS_ENDPOINT.replace("{movie_id}", movieId.toString())
        return handleRequest<CastMembersResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).cast?.map { it.toDto() } ?: emptyList()
    }

    override suspend fun getMoviesByGenre(genreId: Long, page: Int): List<MovieDto> {
        val endpoint = MOVIE_WITH_GENRE_ENDPOINT
        return handleRequest<SimilarMovieResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint?with_genres=$genreId&page=$page"
        ).results.orEmpty().map { it.toDto() }
    }

    override suspend fun getMovieReviews(movieId: Long): List<ReviewDto> {
        val endpoint = MOVIE_REVIEWS_ENDPOINT.replace("{movie_id}", movieId.toString())
        return handleRequest<ReviewsResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).results.orEmpty().map { it.toDto() }
    }

    override suspend fun getMovieImages(movieId: Long): List<String> {
        val endpoint = MOVIE_IMAGES_ENDPOINT.replace("{movie_id}", movieId.toString())
        return handleRequest<MovieImageResponse>(
            client = httpClient,
            url = "$baseUrl$endpoint"
        ).backdrops?.map { it.filePath.orEmpty() }.orEmpty()
    }

    companion object {
        private const val SIMILAR_MOVIES_ENDPOINT = "/movie/{movie_id}/similar"
        private const val MOVIE_DETAILS_ENDPOINT = "/movie/{movie_id}"
        private const val MOVIE_CREDITS_ENDPOINT = "/movie/{movie_id}/credits"
        private const val MOVIE_WITH_GENRE_ENDPOINT = "/discover/movie"
        private const val MOVIE_REVIEWS_ENDPOINT = "/movie/{movie_id}/reviews"
        private const val MOVIE_IMAGES_ENDPOINT = "/movie/{movie_id}/images"
    }
}
