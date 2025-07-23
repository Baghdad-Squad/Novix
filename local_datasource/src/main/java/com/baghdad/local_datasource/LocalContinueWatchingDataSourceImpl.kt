package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ContinueWatchingDto

class LocalContinueWatchingDataSourceImpl(
    private val continueWatchingDao: ContinueWatchingDao,
    private val logger: Logger
) : LocalContinueWatchingDataSource {
    override suspend fun addContinueWatching(continueWatching: ContinueWatchingDto) =
        executeWithErrorHandling(logger = logger) {
            continueWatchingDao.upsertContinueWatching(continueWatching.toLocalDto())
        }

    override suspend fun getContinueWatching(userId: Long , pageSize: Int , page: Int): List<ContinueWatchingDto> {
        val pageOffset = calculatePageOffset(pageSize, page)
        return executeWithErrorHandling(logger = logger) {
            continueWatchingDao.getContinueWatching(userId, pageSize = pageSize, offset = pageOffset)
                .map { it.toDto() }
        }
    }

}