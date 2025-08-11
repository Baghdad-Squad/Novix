package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.interceptor.RequiresSession
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EpisodeApiService {

    @Authenticated
    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<EpisodeDetailsResponse>

    @Authenticated
    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/credits")
    suspend fun getEpisodeCastMembers(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<CastMembersResponse>

    @Authenticated
    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/images")
    suspend fun getEpisodeImages(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<EpisodeImageResponse>

    @Authenticated
    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/videos")
    suspend fun getEpisodeTrailer(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<EpisodeVideosResponse>

    @Authenticated
    @RequiresSession
    @POST(" tv/{series_id}/season/{season_number}/episode/{episode_number}/rating")
    suspend fun addEpisodeRate(
        @Path("series_id") seriesId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Body rating: RatingRequest
    ): Response<RatingResponse>

    @Authenticated
    @RequiresSession
    @GET("tv/{series_id}/season/{season_number}/episode/{episode_number}/account_states")
    suspend fun getEpisodeAccountStates(
        @Path("series_id") seriesId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): Response<MediaAccountStatesResponse>

}