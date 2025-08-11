package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.Cacheable
import com.baghdad.remoteDataSource.interceptor.ForceLocaleEnglish
import com.baghdad.remoteDataSource.interceptor.RequiresSession
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.response.movie.MovieImageResponse
import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.remoteDataSource.response.movie.SimilarMovieResponse
import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @Authenticated
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Long
    ): Response<SimilarMovieResponse>

    @Authenticated
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long
    ): Response<MovieDetailsResponse>

    @Authenticated
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCastMembers(
        @Path("movie_id") movieId: Long
    ): Response<CastMembersResponse>

    @Authenticated
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Long,
        @Query("page") page: Int
    ): Response<DiscoverMovieResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Long
    ): Response<ReviewsResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Long
    ): Response<MovieImageResponse>

    @Authenticated
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(
        @Path("movie_id") movieId: Long
    ): Response<MovieVideosResponse>

    @Authenticated
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int
    ): Response<TrendingMovieResponse>

    @Cacheable
    @Authenticated
    @GET("discover/movie")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVoteCount: Int = 200,
    ): Response<DiscoverMovieResponse>

    @Cacheable
    @Authenticated
    @GET("discover/movie")
    suspend fun getUpcomingMovies(
        @Query("with_genres") genres: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_release_type") releaseType: String = "2|3",
        @Query("primary_release_date.gte") releaseDateGte: String,
        @Query("primary_release_date.lte") releaseDateLte: String,
    ): Response<DiscoverMovieResponse>

    @Cacheable
    @Authenticated
    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<PopularMoviesResponse>

    @Authenticated
    @RequiresSession
    @POST("movie/{movie_id}/rating")
    suspend fun addMovieRate(
        @Path("movie_id") movieId: Long,
        @Body rating: RatingRequest
    ): Response<RatingResponse>

    @Authenticated
    @RequiresSession
    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteMovieRate(
        @Path("movie_id") movieId: Long,
    ): Response<RatingResponse>

    @Authenticated
    @RequiresSession
    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieAccountStates(
        @Path("movie_id") movieId: Long,
    ): Response<MediaAccountStatesResponse>

    @Authenticated
    @RequiresSession
    @GET("account/{account_id}/rated/movies")
    suspend fun getUserRatedMovies(
        @Path("account_id") accountId: Long,
        @Query("page") page: Int
    ): Response<MyRatingMoviesResponse>
}