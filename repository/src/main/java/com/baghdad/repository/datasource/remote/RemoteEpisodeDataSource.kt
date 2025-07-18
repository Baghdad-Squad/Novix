package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto

interface RemoteEpisodeDataSource {
    suspend fun getEpisodeDetails(tvId: Long, seasonNumber: Int, episodeNumber: Int): EpisodeDto
    suspend fun getEpisodeCastMembers(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<CastMemberDto>
    suspend fun getEpisodeImages(tvId: Long, seasonNumber: Int, episodeNumber: Int): List<String>
}