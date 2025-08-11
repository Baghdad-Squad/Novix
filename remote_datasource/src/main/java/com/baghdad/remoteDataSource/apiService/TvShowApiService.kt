package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.Cacheable
import com.baghdad.remoteDataSource.interceptor.ForceLocaleEnglish
import com.baghdad.remoteDataSource.interceptor.RequiresSession
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.MyRatingTvShowResponse
import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface TvShowApiService {

    @Authenticated
    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Long
    ): Response<TVShowDetailsResponse>

    @Authenticated
    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCastMembers(
        @Path("tv_id") tvId: Long
    ): Response<CastMembersResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET("tv/{tv_id}/images")
    suspend fun getTvShowImages(
        @Path("tv_id") tvId: Long
    ): Response<TVShowImagesResponse>

    @Authenticated
    @GET("discover/tv")
    suspend fun getTvShowsByGenre(
        @Query("with_genres") genreId: Long,
        @Query("page") page: Int
    ): Response<TvShowResponse>

    @Authenticated
    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getTvShowEpisodes(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int
    ): Response<SeasonDetailResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET("tv/{tv_id}/reviews")
    suspend fun getTvShowReviews(
        @Path("tv_id") tvId: Long
    ): Response<ReviewsResponse>

    @Authenticated
    @GET("tv/{tv_id}/videos")
    suspend fun getTvShowTrailer(
        @Path("tv_id") tvId: Long
    ): Response<TVShowVideosResponse>

    @Authenticated
    @GET("discover/tv")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVoteCount: Int = 200,
    ): Response<TopRatedTvShowSearchResponse>

    @Cacheable
    @Authenticated
    @GET("tv/popular")
    suspend fun getPopularTvShows(): Response<PopularTvShowsResponse>

    @Authenticated
    @GET("trending/tv/day")
    suspend fun getTrendingTvShows(
        @Query("page") page: Int
    ): Response<TrendingTvShowsResponse>

    @Authenticated
    @RequiresSession
    @POST("tv/{series_id}/rating")
    suspend fun addTvShowRate(
        @Path("series_id") seriesId: Long,
        @Body rating: RatingRequest
    ): Response<RatingResponse>

    @Authenticated
    @RequiresSession
    @DELETE("tv/{series_id}/rating")
    suspend fun deleteTvShowRate(
        @Path("series_id") seriesId: Long,
    ): Response<RatingResponse>

    @Authenticated
    @RequiresSession
    @GET("tv/{series_id}/account_states")
    suspend fun getTvShowAccountStates(
        @Path("series_id") seriesId: Long,
    ): Response<MediaAccountStatesResponse>

    @Authenticated
    @RequiresSession
    @GET("account/{account_id}/rated/tv")
    suspend fun getUserRatedTvShows(
        @Path("account_id") accountId: Long,
        @Query("page") page: Int
    ): Response<MyRatingTvShowResponse>
}