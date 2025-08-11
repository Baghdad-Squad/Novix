package com.baghdad.repository

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntities
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepositoryImpl @Inject constructor(
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
            val tvShowImages = remoteTvShowDataSource.getTvShowImages(tvId)
            val tvShowGenres = remoteTvShowDataSource.getTvShowDetails(tvId).genres
            remoteEpisodeDataSource.getEpisodeDetails(tvId, seasonNumber, episodeNumber).toEntity()
                .copy(
                    trailerUrl = trailerUrl,
                    headerPictures = tvShowImages,
                    genres = tvShowGenres.toEntities()
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

    override suspend fun addTvEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ) {
        executeSafely(
            block = {
                remoteEpisodeDataSource.addEpisodeRate(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    rating = rating
                )
            }
        )
    }

    override suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): MediaAccountStates {
        return executeSafely(
            block = {
                remoteEpisodeDataSource.getEpisodeAccountStates(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                ).toEntity()
            }
        )
    }
}