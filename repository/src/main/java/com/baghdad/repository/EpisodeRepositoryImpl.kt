package com.baghdad.repository

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely

class EpisodeRepositoryImpl(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : EpisodeRepository {
    override suspend fun getEpisodeDetails(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode {
        return executeSafely {
            val trailerUrl =
                remoteEpisodeDataSource.getEpisodeTrailer(tvId, seasonNumber, episodeNumber)
            val tvShowImages = remoteTvShowDataSource.getTvShowImages(tvId).take(MAX_TV_SHOW_IMAGES)
            remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber).toEntity()
                .copy(
                    trailerUrl = trailerUrl,
                    headerPictures = tvShowImages
                )
        }
    }

    override suspend fun getEpisodeCastMembers(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMember> {
        return executeSafely {
            remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
                .map { it.toEntity() }
        }
    }

    companion object {
        const val MAX_TV_SHOW_IMAGES = 10
    }
}