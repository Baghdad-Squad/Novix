package com.baghdad.repository.datasource.remote

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.EpisodeDto
import com.baghdad.repository.model.MediaAccountStateDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.ReviewDto
import com.baghdad.repository.model.TvShowDto

interface RemoteTvShowDataSource {
    suspend fun getPopularTvShows(): List<TvShowDto>

    suspend fun getTvShowDetails(tvShowId: Long): TvShowDto

    suspend fun getTvShowCastMembers(tvShowId: Long): List<CastMemberDto>

    suspend fun getTvShowImages(tvShowId: Long): List<String>

    suspend fun getTvShowReviews(tvShowId: Long): List<ReviewDto>

    suspend fun getTvShowTrailer(tvShowId: Long): String

    suspend fun getTopRatedTvShows(page: Int): PagedResultDto<TvShowDto>

    suspend fun getTrendingTvShows(page: Int): PagedResultDto<TvShowDto>

    suspend fun deleteTvShowRate(tvShowId: Long)

    suspend fun getTvShowsByGenre(genreId: Long, page: Int): PagedResultDto<TvShowDto>

    suspend fun getTvShowEpisodes(tvShowId: Long, seasonNumber: Int): List<EpisodeDto>

    suspend fun getTvShowAccountStates(tvShowId: Long): MediaAccountStateDto

    suspend fun addTvShowRate(tvShowId: Long, rating: Int)

    suspend fun getUserRatedTvShows(accountId: Long, page: Int): PagedResultDto<TvShowDto>
}