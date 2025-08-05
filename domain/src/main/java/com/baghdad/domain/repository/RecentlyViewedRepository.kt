package com.baghdad.domain.repository

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.entity.media.Genre
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedRepository {
    suspend fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewed, mediaGenres: List<Genre>)
}