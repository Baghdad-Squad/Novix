package com.baghdad.repository

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntities
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toIsMediaRated
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepositoryImpl @Inject constructor(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : EpisodeRepository {
    override suspend fun getEpisodeCastMembers(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMember> {
        return executeSafely {
            remoteEpisodeDataSource.getEpisodeCastMembers(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            )
                .map { it.toEntity() }
        }
    }

    override suspend fun addTvEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ) {
        executeSafely {
            remoteEpisodeDataSource.addEpisodeRate(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                rating = rating,
            )
        }
    }

    override suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
    ): Boolean {
        return executeSafely {
            remoteEpisodeDataSource.getEpisodeAccountStates(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
            ).toIsMediaRated()
        }
    }

    override suspend fun getEpisodeDetails(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode {
        return executeSafely {
            val trailerUrl =
                remoteEpisodeDataSource.getEpisodeTrailer(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            val tvShowImages = remoteTvShowDataSource.getTvShowImages(tvShowId = tvShowId)
            val tvShowGenres =
                remoteTvShowDataSource.getTvShowDetails(tvShowId = tvShowId).genres
            remoteEpisodeDataSource.getEpisodeDetails(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            ).toEntity()
                .copy(
                    trailerUrl = trailerUrl,
                    headerPictures = tvShowImages,
                    genres = tvShowGenres.toEntities()
                )
        }
    }
}