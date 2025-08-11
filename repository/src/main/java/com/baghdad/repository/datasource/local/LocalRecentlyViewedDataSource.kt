package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.RecentlyViewedDto
import kotlinx.coroutines.flow.Flow

interface LocalRecentlyViewedDataSource {
    suspend fun deleteAllRecentlyViewed()

    fun getAllRecentlyViewed(): Flow<List<RecentlyViewedDto>>

    suspend fun addMediaToRecentlyViewed(recentlyViewedDto: RecentlyViewedDto)
}