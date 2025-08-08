package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.episode.mapToYoutubeTrailerUrl
import com.baghdad.remoteDataSource.mapper.episode.toDto
import com.baghdad.remoteDataSource.mapper.toDto
import com.baghdad.remoteDataSource.request.RatingRequest
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.MediaAccountStatesResponse
import com.baghdad.remoteDataSource.response.RatingResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
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
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodeDto {
        return handleRequest<EpisodeDetailsResponse>(
            apiCall = {
                episodeApiService.getEpisodeDetails(
                    tvId = tvId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            logger = logger,
            ).toDto()
    }

    override suspend fun getEpisodeCastMembers(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMemberDto> {
        return handleRequest<CastMembersResponse>(
            apiCall = {
                episodeApiService.getEpisodeCastMembers(
                    tvId = tvId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            logger = logger,
        ).cast?.mapNotNull { it.takeIf { it.id != null }?.toDto() } ?: emptyList()
    }

    override suspend fun getEpisodeImages(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> {
        return handleRequest<EpisodeImageResponse>(
            apiCall = {
                episodeApiService.getEpisodeImages(
                    tvId = tvId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            logger = logger,
        ).stills.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }

    override suspend fun getEpisodeTrailer(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): String {
        return handleRequest<EpisodeVideosResponse>(
            apiCall = {
                episodeApiService.getEpisodeTrailer(
                    tvId = tvId,
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
        sessionId: String,
        rating: Int
    ) {
        handleRequest<RatingResponse>(
            apiCall = {
                episodeApiService.addEpisodeRate(
                    seriesId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    sessionId = sessionId,
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
        sessionId: String,
    ): MediaAccountStateDto {
        return handleRequest<MediaAccountStatesResponse>(
            apiCall = {
                episodeApiService.getEpisodeAccountStates(
                    seriesId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    sessionId = sessionId
                )
            },
            logger = logger
        ).toDto()
    }
}