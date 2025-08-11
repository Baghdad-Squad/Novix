package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.ForceLocaleEnglish
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @Authenticated
    @GET(MOVIE_DETAILS_ENDPOINT)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long
    ): Response<MovieDetailsResponse>

    @Authenticated
    @GET(MOVIE_CREDITS_ENDPOINT)
    suspend fun getMovieCastMembers(
        @Path("movie_id") movieId: Long
    ): Response<CastMembersResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET(MOVIE_REVIEWS_ENDPOINT)
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Long
    ): Response<ReviewsResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET(MOVIE_IMAGES_ENDPOINT)
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Long
    ): Response<MovieImageResponse>

    @Authenticated
    @GET(MOVIE_VIDEOS_ENDPOINT)
    suspend fun getMovieTrailer(
        @Path("movie_id") movieId: Long
    ): Response<MovieVideosResponse>

    @Authenticated
    @GET(TRENDING_MOVIE_ENDPOINT)
    suspend fun getTrendingMovies(
        @Query("page") page: Int
    ): Response<TrendingMovieResponse>

    @Authenticated
    @GET(DISCOVER_MOVIES_ENDPOINT)
    suspend fun getUpcomingMovies(
        @Query("with_genres") genres: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_release_type") releaseType: String = "2|3",
        @Query("primary_release_date.gte") releaseDateGte: String,
        @Query("primary_release_date.lte") releaseDateLte: String,
    ): Response<DiscoverMovieResponse>

    @Authenticated
    @GET(POPULAR_MOVIES_ENDPOINT)
    suspend fun getPopularMovies(): Response<PopularMoviesResponse>

    @Authenticated
    @POST(RATE_MOVIE_ENDPOINT)
    suspend fun addMovieRate(
        @Path("movie_id") movieId: Long,
        @Query("session_id") sessionId: String,
        @Body rating: RatingRequest
    ): Response<RatingResponse>

    @Authenticated
    @DELETE(RATE_MOVIE_ENDPOINT)
    suspend fun deleteMovieRate(
        @Path("movie_id") movieId: Long,
        @Query("session_id") sessionId: String,
    ): Response<RatingResponse>

    @Authenticated
    @GET(MOVIE_ACCOUNT_STATES)
    suspend fun getMovieAccountStates(
        @Path("movie_id") movieId: Long,
        @Query("session_id") sessionId: String
    ): Response<MediaAccountStatesResponse>

    @Authenticated
    @GET(USER_RATED_MOVIES_ENDPOINT)
    suspend fun getUserRatedMovies(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): Response<MyRatingMoviesResponse>


    companion object {
        private const val SIMILAR_MOVIES_ENDPOINT = "movie/{movie_id}/similar"
        private const val MOVIE_DETAILS_ENDPOINT = "movie/{movie_id}"
        private const val MOVIE_CREDITS_ENDPOINT = "movie/{movie_id}/credits"
        private const val MOVIE_WITH_GENRE_ENDPOINT = "discover/movie"
        private const val MOVIE_REVIEWS_ENDPOINT = "movie/{movie_id}/reviews"
        private const val MOVIE_IMAGES_ENDPOINT = "movie/{movie_id}/images"
        private const val MOVIE_VIDEOS_ENDPOINT = "movie/{movie_id}/videos"
        private const val TRENDING_MOVIE_ENDPOINT = "trending/movie/day"
        private const val DISCOVER_MOVIES_ENDPOINT = "discover/movie"
        private const val POPULAR_MOVIES_ENDPOINT = "movie/popular"
        private const val RATE_MOVIE_ENDPOINT = "movie/{movie_id}/rating"
        private const val MOVIE_ACCOUNT_STATES = "movie/{movie_id}/account_states"
        private const val USER_RATED_MOVIES_ENDPOINT = "account/{account_id}/rated/movies"
    }
}