package com.baghdad.local_datasource

import com.baghdad.local_datasource.roomDB.dao.ContinueWatchingDao
import com.baghdad.local_datasource.roomDB.entity.ContinueWatching
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toDtos
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.local_datasource.roomDB.errorHandler.executeFlowWithErrorHandling
import com.baghdad.local_datasource.roomDB.errorHandler.executeWithErrorHandling
import com.baghdad.local_datasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.ContinueWatchingDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalContinueWatchingDataSourceImpl @Inject constructor(
    private val continueWatchingDao: ContinueWatchingDao,
    private val logger: Logger,
) : LocalContinueWatchingDataSource {
    override suspend fun addContinueWatching(continueWatching: ContinueWatchingDto) =
        executeWithErrorHandling(logger = logger) {
            continueWatchingDao.upsertContinueWatching(continueWatching.toLocalDto())
        }

    override suspend fun getPagedContinueWatchingMovies(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<ContinueWatchingDto> {
        val pageOffset = calculatePageOffset(pageSize, page)
        return executeWithErrorHandling(logger = logger) {
            continueWatchingDao
                .getPagedContinueWatchingMovies(
                    userId,
                    pageSize = pageSize,
                    offset = pageOffset,
                ).map { it.toDto() }
        }
    }

    override suspend fun getPagedContinueWatchingTvShows(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<ContinueWatchingDto> {
        val pageOffset = calculatePageOffset(pageSize, page)
        return executeWithErrorHandling(logger = logger) {
            continueWatchingDao
                .getPagedContinueWatchingTvShows(
                    userId,
                    pageSize = pageSize,
                    offset = pageOffset,
                ).map { it.toDto() }
        }
    }

    override fun observeContinueWatching(userId: Long): Flow<List<ContinueWatchingDto>> =
        executeFlowWithErrorHandling(logger = logger) {
            continueWatchingDao.observeContinueWatching(userId).map(List<ContinueWatching>::toDtos)
        }

    override fun getAllContinueWatchingMovies(userId: Long): Flow<List<ContinueWatchingDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            continueWatchingDao.getAllContinueWatchingMovies(userId)
                .map(List<ContinueWatching>::toDtos)
        }
    }

    override fun getAllContinueWatchingTvShows(userId: Long): Flow<List<ContinueWatchingDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            continueWatchingDao.getAllContinueWatchingTvShows(userId)
                .map(List<ContinueWatching>::toDtos)
        }
    }
}
