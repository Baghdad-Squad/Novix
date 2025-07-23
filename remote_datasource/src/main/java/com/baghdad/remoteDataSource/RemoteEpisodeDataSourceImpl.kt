package com.baghdad.remoteDataSource

import com.baghdad.remoteDataSource.apiService.EpisodeApiService
import com.baghdad.remoteDataSource.mapper.actor.toDto
import com.baghdad.remoteDataSource.mapper.episode.mapToYoutubeTrailerUrl
import com.baghdad.remoteDataSource.mapper.episode.toDto
import com.baghdad.remoteDataSource.response.CastMembersResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.baghdad.remoteDataSource.util.handleRequest
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto

class RemoteEpisodeDataSourceImpl(
    private val episodeApiService: EpisodeApiService,
    private val logger: Logger,
): RemoteEpisodeDataSource {

    override suspend fun getEpisodeDetails(tvId: Long, seasonNumber: Int, episodeNumber: Int): EpisodeDto {
        return handleRequest<EpisodeDetailsResponse>(
            apiCall = {episodeApiService.getEpisodeDetails(
                tvId = tvId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            )},
            logger = logger,

        ).toDto()
    }

    override suspend fun getEpisodeCastMembers(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<CastMemberDto> {
        return handleRequest<CastMembersResponse>(
            apiCall = { episodeApiService.getEpisodeCastMembers(
                tvId = tvId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            ) },
            logger = logger,
        ).cast?.map { it.toDto() } ?: emptyList()

    }

    override suspend fun getEpisodeImages(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<String> {
        return handleRequest<EpisodeImageResponse>(
            apiCall = {episodeApiService.getEpisodeImages(
                tvId = tvId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            )},
            logger = logger,
        ).stills.orEmpty().map { "https://image.tmdb.org/t/p/w500" + it.filePath }
    }

    override suspend fun getEpisodeTrailer(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): String {
        return handleRequest<EpisodeVideosResponse>(
            apiCall = {episodeApiService.getEpisodeTrailer(
                tvId = tvId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            )},
            logger = logger,
        ).mapToYoutubeTrailerUrl()
    }

}