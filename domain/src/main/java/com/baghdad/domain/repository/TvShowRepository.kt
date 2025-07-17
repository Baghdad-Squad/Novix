package com.baghdad.domain.repository

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

    suspend fun getTvShowByGenre(genreId: Long, page: Int): List<TvShow>

    suspend fun getTvShowEpisodes(tvId: Long, seasonNumber: Int): List<Episode>

    suspend fun getTvShowReviews(tvId: Long): List<Review>

}
