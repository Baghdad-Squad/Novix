package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.ReviewsResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowDetailsResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowImagesResponse
import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.baghdad.remoteDataSource.response.tvShow.TvShowResponse
import retrofit2.Response
import retrofit2.http.GET
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

    @Authenticated
    @GET(TV_SHOW_IMAGES_ENDPOINT)
    suspend fun getTvShowImages(
        @Path("tv_id") tvId: Long
    ): Response<TVShowImagesResponse>

    @Authenticated
    @GET(TV_SHOW_WITH_GENRE_ENDPOINT)
    suspend fun getTvShowsByGenre(
        @Query("genre_id") genreId: Long,
        @Query("page") page: Int
    ): Response<TvShowResponse>

    @Authenticated
    @GET(TV_SHOW_EPISODES_ENDPOINT)
    suspend fun getTvShowEpisodes(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int
    ): Response<SeasonDetailResponse>

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

    companion object {
        private const val TV_SHOW_DETAILS_ENDPOINT = "tv/{tv_id}"
        private const val TV_SHOW_CREDITS_ENDPOINT = "tv/{tv_id}/credits"
        private const val TV_SHOW_IMAGES_ENDPOINT = "tv/{tv_id}/images"
        private const val TV_SHOW_EPISODES_ENDPOINT = "tv/{tv_id}/season/{season_number}"
        private const val TV_SHOW_WITH_GENRE_ENDPOINT = "discover/tv"
        private const val TV_SHOW_REVIEWS_ENDPOINT = "tv/{tv_id}/reviews"
        private const val TV_SHOW_VIDEOS_ENDPOINT = "tv/{tv_id}/videos"
    }
}