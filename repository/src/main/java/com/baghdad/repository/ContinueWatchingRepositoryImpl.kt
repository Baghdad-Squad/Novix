package com.baghdad.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.ContinueWatchingDto
import com.baghdad.repository.util.getLocalPaged

class ContinueWatchingRepositoryImpl(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource
): ContinueWatchingRepository {
    override suspend fun getContinueWatching(
        page: Int,
        pageSize: Int
    ): PagedResult<ContinueWatching> {
        return getLocalPaged(
            page = page,
            pageSize = pageSize,
            onStart = { },
            getCachedPage = { _, _ ->
                localContinueWatchingDataSource.getContinueWatching(1) // TODO : Add Authentication here
            },
            mapToEntity = ContinueWatchingDto::toEntity
        )
    }

    override suspend fun addContinueWatching(continueWatching: ContinueWatching) {
        localContinueWatchingDataSource.addContinueWatching(continueWatching.toDto())
    }
}