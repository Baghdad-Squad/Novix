package com.baghdad.domain.repository

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember

interface TvShowRepository {
    suspend fun getTvShowDetails(tvShowId: Long): TvShow

    suspend fun getTvShowCastMembers(tvShowId: Long): List<CastMember>

    suspend fun getTvShowSeasonEpisodes(tvShowId: Long, seasonNumber: Int): List<Episode>

    suspend fun getGenres() : List<Genre>

    suspend fun getTvShowsByGenres(genreId: Long): List<TvShow>
}