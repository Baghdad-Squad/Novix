package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.repository.datasource.local.LocalRecentlyViewedDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.RecentlyViewedDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecentlyViewedDataSourceImpl @Inject constructor(
    private val recentlyViewedDao: RecentlyViewedDao,
    private val logger: Logger
) : LocalRecentlyViewedDataSource {
    override fun getAllRecentlyViewed(): Flow<List<RecentlyViewedDto>> =
        executeFlowWithErrorHandling(logger = logger) {
            recentlyViewedDao.getAllRecentlyViewed().map { it.map(RecentlyViewed::toDto) }
        }

    override suspend fun deleteAllRecentlyViewed() =
        executeWithErrorHandling(logger = logger) {
            recentlyViewedDao.clearAllRecentlyViewed()
        }

    override suspend fun addMediaToRecentlyViewed(recentlyViewedDto: RecentlyViewedDto) =
        executeWithErrorHandling(logger = logger) {
            recentlyViewedDao.upsertRecentlyViewed(recentlyViewedDto.toLocalDto())
        }
}