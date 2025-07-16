package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.TvShowDto

interface RemoteTvShowDataSource {

    suspend fun getTvShowDetails(tvId: Long): TvShowDto

    suspend fun getTvShowCastMembers(tvId: Long): List<CastMemberDto>

    suspend fun getTvShowImages(tvId: Long): List<String>

    suspend fun getTvShowByGenre(genreId: Long, page: Int): List<TvShowDto>

    suspend fun getTvShowEpisodes(tvId: Long, seasonNumber: Int): List<TvShowDto>
}
