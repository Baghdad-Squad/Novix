package com.baghdad.local_datasource

import com.baghdad.local_datasource.database.dao.RecentlyViewedDao
import com.baghdad.local_datasource.database.dto.LocalRecentlyViewedDto
import com.baghdad.local_datasource.database.dto.toDto
import com.baghdad.local_datasource.database.dto.toEntity
import com.baghdad.local_datasource.database.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.database.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.model.RecentlyViewedDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalRecentlyViewedDataSourceImpl(
    private val recentlyViewedDao: RecentlyViewedDao,
) : LocalRecentlyViewedDataSource {

    override fun getAllRecentlyViewed(): Flow<List<RecentlyViewedDto>> {
        return executeFlowWithErrorHandling {
            recentlyViewedDao.getAllRecentlyViewed().map { it.map(LocalRecentlyViewedDto::toDto) }
        }
    }

    override suspend fun deleteAllRecentlyViewed() {
        executeWithErrorHandling {
            recentlyViewedDao.clearAllRecentlyViewed()
        }
    }

    override suspend fun addMediaToRecentlyViewed(recentlyViewedDto: RecentlyViewedDto) {
        executeWithErrorHandling {
            recentlyViewedDao.upsertRecentlyViewed(recentlyViewedDto.toEntity())
        }
    }

}