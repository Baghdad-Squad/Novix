package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto

interface RemoteTvShowDataSource {

    suspend fun getTvShowDetails(tvId: Long): TvShowDto

    suspend fun getTvShowCastMembers(tvId: Long): List<CastMemberDto>

    suspend fun getTvShowImages(tvId: Long): List<String>

    suspend fun getTvShowsByGenre(genreId: Long, page: Int): List<TvShowDto>

    suspend fun getTvShowEpisodes(tvId: Long, seasonNumber: Int): List<EpisodeDto>

    suspend fun getTvShowReviews(tvId: Long): List<ReviewDto>

    suspend fun getTvShowTrailer(tvId: Long): String

    suspend fun getTopRatedTvShows(page: Int): PagedResultDto<TvShowDto>

    suspend fun getTrendingTvShows(page: Int): PagedResultDto<TvShowDto>
}
