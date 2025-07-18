package com.baghdad.repository

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely

class EpisodeRepositoryImpl(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource
) : EpisodeRepository {
    override suspend fun getEpisodeDetails(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode {
        return executeSafely {
            remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber).toEntity()
                .copy(
                    headerPictures = remoteEpisodeDataSource.getEpisodeImages(
                        tvId,
                        seasonNumber,
                        episodeNumber
                    ).take(3)
                )
        }
    }

    override suspend fun getEpisodeCastMembers(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMember> {
        return executeSafely() {
            remoteEpisodeDataSource.getEpisodeCastMembers(tvId, seasonNumber, episodeNumber)
                .map { it.toEntity() }
        }
    }


}