package com.baghdad.domain.repository

import com.baghdad.domain.model.PagedResult
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun deleteRecentSearchById(id : Long)
    suspend fun deleteAllRecentSearches()
    suspend fun searchActorsByName(name: String, page: Int, pageSize: Int = 20): PagedResult<Actor>
    suspend fun searchMoviesByTitle(
        title: String,
        page: Int,
        pageSize: Int = 20
    ): PagedResult<Movie>

    suspend fun searchTvShowsByName(
        title: String,
        page: Int,
        pageSize: Int = 20
    ): PagedResult<TvShow>
}