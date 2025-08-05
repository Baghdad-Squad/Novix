package com.baghdad.domain.repository

import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember

interface EpisodeRepository {
    suspend fun getEpisodeDetails(tvId: Long, seasonNumber: Int, episodeNumber: Int): Episode
    suspend fun getEpisodeCastMembers(
        tvId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMember>

    suspend fun addTvEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    )

    suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): MediaAccountStates
}