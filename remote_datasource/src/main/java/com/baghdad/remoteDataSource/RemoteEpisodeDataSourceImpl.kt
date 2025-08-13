package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.mapper.castMembers.toCastMembers
import com.baghdad.remoteDataSource.mapper.episode.mapToYoutubeTrailerUrl
import com.baghdad.remoteDataSource.mapper.episode.toDto
import com.baghdad.remoteDataSource.mapper.mediaAccountStates.toDto
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.response.mediaAccount.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.rate.RatingResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.MediaAccountStateDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteEpisodeDataSourceImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val logger: Logger,
) : RemoteEpisodeDataSource {

    override suspend fun getEpisodeDetails(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodeDto {
        return handleRequest<EpisodeDetailsResponse>(
            apiCall = {
                episodeApiService.getEpisodeDetails(
                    tvId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            logger = logger,
            ).toDto()
    }

    override suspend fun getEpisodeCastMembers(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMemberDto> {
        return handleRequest<CastMembersResponse>(
            apiCall = {
                episodeApiService.getEpisodeCastMembers(
                    tvId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            logger = logger,
        ).toCastMembers()
    }
    override suspend fun getEpisodeTrailer(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): String {
        return handleRequest<EpisodeVideosResponse>(
            apiCall = {
                episodeApiService.getEpisodeTrailer(
                    tvId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            logger = logger,
        ).mapToYoutubeTrailerUrl()
    }

    override suspend fun addEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ) {
        handleRequest<RatingResponse>(
            apiCall = {
                episodeApiService.addEpisodeRate(
                    seriesId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    rating = RatingRequest(rating)
                )
            },
            logger = logger,
        )
    }

    override suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaAccountStateDto {
        return handleRequest<MediaAccountStatesResponse>(
            apiCall = {
                episodeApiService.getEpisodeAccountStates(
                    seriesId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                )
            },
            logger = logger
        ).toDto()
    }
}