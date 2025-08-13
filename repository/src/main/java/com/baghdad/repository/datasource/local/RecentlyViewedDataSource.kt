package com.baghdad.repository.datasource.local

import com.baghdad.repository.model.RecentlyViewedDto
import kotlinx.coroutines.flow.Flow

interface RecentlyViewedDataSource {
    fun getAllRecentlyViewed(): Flow<List<RecentlyViewedDto>>
    suspend fun deleteAllRecentlyViewed()
    suspend fun addMediaToRecentlyViewed(recentlyViewedDto: RecentlyViewedDto)
}