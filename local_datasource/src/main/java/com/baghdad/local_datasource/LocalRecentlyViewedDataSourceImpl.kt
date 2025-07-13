package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.model.RecentlyViewedDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalRecentlyViewedDataSourceImpl(
    private val recentlyViewedDao: RecentlyViewedDao,
) : LocalRecentlyViewedDataSource {
    override fun getAllRecentlyViewed(): Flow<List<RecentlyViewedDto>> =
        executeFlowWithErrorHandling {
            recentlyViewedDao.getAllRecentlyViewed().map { it.map(RecentlyViewed::toDto) }
        }

    override suspend fun deleteAllRecentlyViewed() =
        executeWithErrorHandling {
            recentlyViewedDao.clearAllRecentlyViewed()
        }

    override suspend fun addMediaToRecentlyViewed(recentlyViewedDto: RecentlyViewedDto) =
        executeWithErrorHandling {
            recentlyViewedDao.upsertRecentlyViewed(recentlyViewedDto.toEntity())
        }

}