package com.baghdad.repository

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.datasource.local.LocalSessionDataSource
import com.baghdad.repository.datasource.remote.RemoteEpisodeDataSource
import com.baghdad.repository.datasource.remote.RemoteTvShowDataSource
import com.baghdad.repository.mapper.toEntities
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.mapper.toIsMediaRated
import com.baghdad.repository.util.executeAuthorizedSafely
import com.baghdad.repository.util.executeSafely
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeRepositoryImpl @Inject constructor(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource,
    private val localSessionDataSource: LocalSessionDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : EpisodeRepository {
    override suspend fun getEpisodeDetails(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Episode {
        return executeSafely {
            val trailerUrl =
                remoteEpisodeDataSource.getEpisodeTrailer(tvShowId, seasonNumber, episodeNumber)
            val tvShowImages = remoteTvShowDataSource.getTvShowImages(tvShowId).take(MAX_TV_SHOW_IMAGES)
            val tvShowGenres = remoteTvShowDataSource.getTvShowDetails(tvShowId).genres
            remoteEpisodeDataSource.getEpisodeDetails(tvShowId, seasonNumber, episodeNumber).toEntity()
                .copy(
                    trailerUrl = trailerUrl,
                    headerPictures = tvShowImages,
                    genres = tvShowGenres.toEntities()
                )
        }
    }

    override suspend fun getEpisodeCastMembers(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMember> {
        return executeSafely {
            remoteEpisodeDataSource.getEpisodeCastMembers(tvShowId, seasonNumber, episodeNumber)
                .map { it.toEntity() }
        }
    }

    override suspend fun addTvEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ) {
        executeAuthorizedSafely(
            sessionId = localSessionDataSource.getSessionId(),
            block = {
                remoteEpisodeDataSource.addEpisodeRate(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    sessionId = it,
                    rating = rating
                )
            }
        )
    }

    override suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): Boolean {
        return executeAuthorizedSafely(
            sessionId = localSessionDataSource.getSessionId(),
            block = {
                remoteEpisodeDataSource.getEpisodeAccountStates(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    sessionId = it
                ).toIsMediaRated()
            }
        )
    }

    companion object {
        private const val MAX_TV_SHOW_IMAGES = 10
    }
}