package com.baghdad.localDatasource

import com.baghdad.localDatasource.errorHandler.executeFlowWithErrorHandling
import com.baghdad.localDatasource.errorHandler.executeWithErrorHandling
import com.baghdad.localDatasource.mapper.toDto
import com.baghdad.localDatasource.mapper.toLocalDto
import com.baghdad.localDatasource.roomDB.dao.RecentlyViewedDao
import com.baghdad.localDatasource.roomDB.entity.RecentlyViewed
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
    override fun getAllRecentlyViewed(): Flow<List<RecentlyViewedDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            recentlyViewedDao.getAllRecentlyViewed().map { it.map(RecentlyViewed::toDto) }
        }
    }

    override suspend fun deleteAllRecentlyViewed() {
        executeWithErrorHandling(logger = logger) {
            recentlyViewedDao.deleteAllRecentlyViewed()
        }
    }

    override suspend fun addMediaToRecentlyViewed(recentlyViewedDto: RecentlyViewedDto) {
        executeWithErrorHandling(logger = logger) {
            recentlyViewedDao.upsertRecentlyViewed(recentlyViewedDto.toLocalDto())
        }
    }

}