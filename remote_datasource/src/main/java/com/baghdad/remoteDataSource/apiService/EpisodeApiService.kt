package com.baghdad.remoteDataSource.apiService

import com.baghdad.remoteDataSource.interceptor.Authenticated
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.RatingResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.response.MediaAccountStatesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeApiService {

    @Authenticated
    @GET(EPISODES_DETAILS_ENDPOINT)
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<EpisodeDetailsResponse>

    @Authenticated
    @GET(EPISODE_CREDITS_ENDPOINT)
    suspend fun getEpisodeCastMembers(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<CastMembersResponse>

    @Authenticated
    @GET(EPISODE_IMAGES_ENDPOINT)
    suspend fun getEpisodeImages(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<EpisodeImageResponse>

    @Authenticated
    @GET(EPISODE_VIDEOS_ENDPOINT)
    suspend fun getEpisodeTrailer(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int
    ): Response<EpisodeVideosResponse>

    @Authenticated
    @POST(RATE_EPISODE_ENDPOINT)
    suspend fun addEpisodeRate(
        @Path("series_id") seriesId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("session_id") sessionId: String,
        @Body rating: RatingRequest
    ): Response<RatingResponse>

    @Authenticated
    @GET(EPISODE_ACCOUNT_STATES)
    suspend fun getEpisodeAccountStates(
        @Path("series_id") seriesId: Long,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("session_id") sessionId: String,
    ): Response<MediaAccountStatesResponse>


    companion object {
        private const val EPISODES_DETAILS_ENDPOINT =
            "tv/{tv_id}/season/{season_number}/episode/{episode_number}"
        private const val EPISODE_CREDITS_ENDPOINT =
            "tv/{tv_id}/season/{season_number}/episode/{episode_number}/credits"
        private const val EPISODE_IMAGES_ENDPOINT =
            "tv/{tv_id}/season/{season_number}/episode/{episode_number}/images"
        private const val EPISODE_VIDEOS_ENDPOINT =
            "tv/{tv_id}/season/{season_number}/episode/{episode_number}/videos"
        private const val RATE_EPISODE_ENDPOINT =
           " tv/{series_id}/season/{season_number}/episode/{episode_number}/rating"
        private const val EPISODE_ACCOUNT_STATES =
            "tv/{series_id}/season/{season_number}/episode/{episode_number}/account_states"
    }
}