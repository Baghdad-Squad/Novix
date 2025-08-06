package com.baghdad.domain.repository

import com.baghdad.domain.model.search.RecentlyViewed
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedRepository {
    fun getAllRecentlyViewed(): Flow<List<RecentlyViewed>>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewed)
}