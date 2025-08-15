package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.MediaAccountStateDto

interface RemoteEpisodeDataSource {
    suspend fun getEpisodeDetails(tvShowId: Long, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeCastMembers(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<CastMemberDto>

    suspend fun getEpisodeTrailer(tvShowId: Long, seasonNumber: Int, episodeNumber: Int): String
    suspend fun addEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    )
    suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
    ): MediaAccountStateDto
}