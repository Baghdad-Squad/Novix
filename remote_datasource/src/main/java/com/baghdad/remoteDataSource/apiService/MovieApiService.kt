package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET(SIMILAR_MOVIES_ENDPOINT)
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Long
    ): Response<SimilarMovieResponse>

    @GET(MOVIE_DETAILS_ENDPOINT)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long
    ): Response<MovieDetailsResponse>

    @GET(MOVIE_CREDITS_ENDPOINT)
    suspend fun getMovieCastMembers(
        @Path("movie_id") movieId: Long
    ): Response<CastMembersResponse>

    @GET(MOVIE_WITH_GENRE_ENDPOINT)
    suspend fun getMoviesByGenre(
        @Query("genre_id") genreId: Long,
        @Query("page") page: Int
    ): Response<SimilarMovieResponse>

    @GET(MOVIE_REVIEWS_ENDPOINT)
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Long
    ): Response<ReviewsResponse>

    @GET(MOVIE_IMAGES_ENDPOINT)
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Long
    ): Response<MovieImageResponse>

    @GET(MOVIE_VIDEOS_ENDPOINT)
    suspend fun getMovieTrailer(
        @Path("movie_id") movieId: Long
    ): Response<MovieVideosResponse>

    companion object {
        private const val SIMILAR_MOVIES_ENDPOINT = "/movie/{movie_id}/similar"
        private const val MOVIE_DETAILS_ENDPOINT = "/movie/{movie_id}"
        private const val MOVIE_CREDITS_ENDPOINT = "/movie/{movie_id}/credits"
        private const val MOVIE_WITH_GENRE_ENDPOINT = "/discover/movie"
        private const val MOVIE_REVIEWS_ENDPOINT = "/movie/{movie_id}/reviews"
        private const val MOVIE_IMAGES_ENDPOINT = "/movie/{movie_id}/images"
        private const val MOVIE_VIDEOS_ENDPOINT = "/movie/{movie_id}/videos"
    }
}