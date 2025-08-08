package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.ForceLocaleEnglish
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.RatingResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.PopularTvShowsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.response.tvShow.TopRatedTvShowSearchResponse
import com.baghdad.remoteDataSource.response.tvShow.TrendingTvShowsResponse
import com.baghdad.remoteDataSource.response.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface TvShowApiService {

    @Authenticated
    @GET(TV_SHOW_DETAILS_ENDPOINT)
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Long
    ): Response<TVShowDetailsResponse>

    @Authenticated
    @GET(TV_SHOW_CREDITS_ENDPOINT)
    suspend fun getTvShowCastMembers(
        @Path("tv_id") tvId: Long
    ): Response<CastMembersResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET(TV_SHOW_IMAGES_ENDPOINT)
    suspend fun getTvShowImages(
        @Path("tv_id") tvId: Long
    ): Response<TVShowImagesResponse>

    @Authenticated
    @GET(TV_SHOW_WITH_GENRE_ENDPOINT)
    suspend fun getTvShowsByGenre(
        @Query("with_genres") genreId: Long,
        @Query("page") page: Int
    ): Response<TvShowResponse>

    @Authenticated
    @GET(TV_SHOW_EPISODES_ENDPOINT)
    suspend fun getTvShowEpisodes(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int
    ): Response<SeasonDetailResponse>

    @ForceLocaleEnglish
    @Authenticated
    @GET(TV_SHOW_REVIEWS_ENDPOINT)
    suspend fun getTvShowReviews(
        @Path("tv_id") tvId: Long
    ): Response<ReviewsResponse>

    @Authenticated
    @GET(TV_SHOW_VIDEOS_ENDPOINT)
    suspend fun getTvShowTrailer(
        @Path("tv_id") tvId: Long
    ): Response<TVShowVideosResponse>

    @Authenticated
    @GET(TV_SHOW_DISCOVER_ENDPOINT)
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") minVoteCount: Int = 200,
    ): Response<TopRatedTvShowSearchResponse>

    @Authenticated
    @GET(POPULAR_TV_SHOWS_ENDPOINT)
    suspend fun getPopularTvShows(): Response<PopularTvShowsResponse>

    @Authenticated
    @GET(TV_SHOW_TRENDING_ENDPOINT)
    suspend fun getTrendingTvShows(
        @Query("page") page: Int
    ): Response<TrendingTvShowsResponse>

    @Authenticated
    @POST(RATE_TV_SHOW_ENDPOINT)
    suspend fun addTvShowRate(
        @Path("series_id") seriesId: Long,
        @Query("session_id") sessionId: String,
        @Body rating: RatingRequest
    ): Response<RatingResponse>

    @Authenticated
    @GET(TV_SHOW_ACCOUNT_STATES)
    suspend fun getTvShowAccountStates(
        @Path("series_id") seriesId: Long,
        @Query("session_id") sessionId: String
    ): Response<MediaAccountStatesResponse>

    companion object {
        private const val TV_SHOW_DETAILS_ENDPOINT = "tv/{tv_id}"
        private const val TV_SHOW_CREDITS_ENDPOINT = "tv/{tv_id}/credits"
        private const val TV_SHOW_IMAGES_ENDPOINT = "tv/{tv_id}/images"
        private const val TV_SHOW_EPISODES_ENDPOINT = "tv/{tv_id}/season/{season_number}"
        private const val TV_SHOW_WITH_GENRE_ENDPOINT = "discover/tv"
        private const val TV_SHOW_REVIEWS_ENDPOINT = "tv/{tv_id}/reviews"
        private const val TV_SHOW_VIDEOS_ENDPOINT = "tv/{tv_id}/videos"
        private const val TV_SHOW_DISCOVER_ENDPOINT = "discover/tv"
        private const val TV_SHOW_TRENDING_ENDPOINT = "trending/tv/day"
        private const val POPULAR_TV_SHOWS_ENDPOINT = "tv/popular"
        private const val RATE_TV_SHOW_ENDPOINT = "tv/{series_id}/rating"
        private const val TV_SHOW_ACCOUNT_STATES = "tv/{series_id}/account_states"
    }
}