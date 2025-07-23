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
        userId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<ContinueWatching> {
        return getLocalPaged(
            page = page,
            pageSize = pageSize,
            onStart = { },
            getCachedPage = { _, _ ->
                localContinueWatchingDataSource.getContinueWatching(userId)
            },
            mapToEntity = ContinueWatchingDto::toEntity
        )
    }

    override suspend fun getMoviesByGenreId(
        userId: Long,
        genreId: Long,
        page: Int,
        pageSize: Int): PagedResult<ContinueWatching>{
        return getLocalPaged(
            page = page,
            pageSize = pageSize,
            onStart = { },
            getCachedPage = { _, _ ->
                localContinueWatchingDataSource.getMoviesByGenreId(userId, genreId)}
            ,
            mapToEntity = ContinueWatchingDto::toEntity
        )

    }

    override suspend fun addContinueWatching(continueWatching: ContinueWatching) {
        localContinueWatchingDataSource.addContinueWatching(continueWatching.toDto())
    }
}