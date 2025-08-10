package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.MediaAccountStateDto

interface RemoteEpisodeDataSource {
    suspend fun getEpisodeDetails(tvId: Long, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeCastMembers(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<CastMemberDto>
    suspend fun getEpisodeImages(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<String>
    suspend fun getEpisodeTrailer(tvId: Long, seasonNumber: Int, episodeNumber: Int): String
    suspend fun addEpisodeRate(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        sessionId: String,
        rating: Int
    )
    suspend fun getEpisodeAccountStates(
        tvShowId: Long,
        seasonNumber: Int,
        episodeNumber: Int,
        sessionId: String,
    ): MediaAccountStateDto
}