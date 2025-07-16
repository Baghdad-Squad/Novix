package com.baghdad.domain.repository

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor

interface TvShowRepository {
    suspend fun getTvShowInfo(tvShowId: Long): TvShow
    suspend fun getTvShowCast(tvShowId: Long): List<Actor>
    suspend fun getTvShowEpisodes(tvShowId: Long): List<Episode>
    suspend fun getGenres() : List<Genre>
}