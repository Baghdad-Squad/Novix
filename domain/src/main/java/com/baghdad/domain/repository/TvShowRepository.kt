package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.MediaAccountStates
import com.baghdad.domain.model.RatedMedia
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Review
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember

interface TvShowRepository {

    suspend fun getGenres(): List<Genre>

    suspend fun getTvShowDetails(tvId: Long): TvShow

    suspend fun getTvShowCastMembers(tvId: Long): List<CastMember>

    suspend fun getTvShowImages(tvId: Long): List<String>

    suspend fun getTvShowsByGenre(genreId: Long, page: Int, pageSize: Int): PagedResult<TvShow>

    suspend fun getTvShowSeasonEpisodes(tvId: Long, seasonNumber: Int): List<Episode>

    suspend fun getTvShowReviews(tvId: Long): List<Review>

    suspend fun getPopularTvShows(): List<TvShow>

    suspend fun getTopRatedTvShows(page: Int): PagedResult<TvShow>

    suspend fun getTrendingTvShows(page: Int): PagedResult<TvShow>

    suspend fun addTvShowRate(tvShowId: Long, rating: Int)

    suspend fun getTvShowAccountStates(tvShowId: Long): MediaAccountStates

    suspend fun getUserRatedTvShows(page: Int , pageSize: Int):  PagedResult<RatedMedia>

    suspend fun deleteTvShowRate(tvShowId: Long)
}
