package com.baghdad.localDatasource

import com.baghdad.localDatasource.errorHandler.executeFlowWithErrorHandling
import com.baghdad.localDatasource.errorHandler.executeWithErrorHandling
import com.baghdad.localDatasource.mapper.toDto
import com.baghdad.localDatasource.mapper.toDtos
import com.baghdad.localDatasource.mapper.toLocalDto
import com.baghdad.localDatasource.roomDB.dao.UserWatchedMediaDao
import com.baghdad.localDatasource.roomDB.entity.UserWatchedMedia
import com.baghdad.localDatasource.util.calculatePageOffset
import com.baghdad.repository.datasource.local.UserWatchedMediaDataSource
import com.baghdad.repository.logger.Logger
import com.baghdad.repository.model.UserWatchedMediaDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserWatchedMediaDataSourceImpl @Inject constructor(
    private val userWatchedMediaDao: UserWatchedMediaDao,
    private val logger: Logger
) : UserWatchedMediaDataSource {
    override suspend fun addUserWatchedMedia(userWatchedMediaDto: UserWatchedMediaDto) {
        executeWithErrorHandling(logger = logger) {
            userWatchedMediaDao.upsertUserWatchedMedia(userWatchedMediaDto.toLocalDto())
        }
    }

    override suspend fun getPagedUserWatchedMediaMovies(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<UserWatchedMediaDto> {
        val pageOffset = calculatePageOffset(pageSize, page)
        return executeWithErrorHandling(logger = logger) {
            userWatchedMediaDao
                .getPagedUserWatchedMediaMovies(
                    userId,
                    pageSize = pageSize,
                    offset = pageOffset,
                ).map { it.toDto() }
        }
    }

    override suspend fun getPagedUserWatchedMediaTvShows(
        userId: Long,
        pageSize: Int,
        page: Int
    ): List<UserWatchedMediaDto> {
        val pageOffset = calculatePageOffset(pageSize, page)
        return executeWithErrorHandling(logger = logger) {
            userWatchedMediaDao
                .getPagedUserWatchedMediaTvShows(
                    userId,
                    pageSize = pageSize,
                    offset = pageOffset,
                ).map { it.toDto() }
        }
    }

    override fun observeUserWatchedMedia(userId: Long): Flow<List<UserWatchedMediaDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            userWatchedMediaDao.observeUserWatchedMedia(userId).map(List<UserWatchedMedia>::toDtos)
        }
    }


    override fun getUserWatchedMediaMovies(userId: Long): Flow<List<UserWatchedMediaDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            userWatchedMediaDao.getUserWatchedMediaMovies(userId)
                .map(List<UserWatchedMedia>::toDtos)
        }
    }

    override fun getUserWatchedMediaTvShows(userId: Long): Flow<List<UserWatchedMediaDto>> {
        return executeFlowWithErrorHandling(logger = logger) {
            userWatchedMediaDao.getUserWatchedMediaTvShows(userId)
                .map(List<UserWatchedMedia>::toDtos)
        }
    }
}
