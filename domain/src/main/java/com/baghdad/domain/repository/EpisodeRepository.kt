package com.baghdad.domain.repository

import com.baghdad.entity.media.Episode
import com.baghdad.entity.person.CastMember

interface EpisodeRepository {

    suspend fun getEpisodeDetails(tvShowId: Long, seasonNumber: Int, episodeNumber: Int): Episode

    suspend fun getEpisodeCastMembers(
        tvShowId: Long,
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
    ): Boolean
}