package com.baghdad.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import com.baghdad.repository.datasource.local.LocalContinueWatchingDataSource
import com.baghdad.repository.mapper.toDto
import com.baghdad.repository.mapper.toEntities
import com.baghdad.repository.mapper.toEntity
import com.baghdad.repository.model.ContinueWatchingDto
import com.baghdad.repository.util.executeSafely
import com.baghdad.repository.util.getLocalPagedSafely
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContinueWatchingRepositoryImpl(
    private val localContinueWatchingDataSource: LocalContinueWatchingDataSource,
) : ContinueWatchingRepository {
    override suspend fun getContinueWatching(
        page: Int,
        pageSize: Int,
    ): PagedResult<ContinueWatching> =
        getLocalPagedSafely(
            page = page,
            pageSize = pageSize,
            onStart = { },
            getCachedPage = { _, _ ->
                localContinueWatchingDataSource.getContinueWatching(
                    1,
                    pageSize,
                    page,
                ) // TODO : Add Authentication here
            },
            mapToEntity = ContinueWatchingDto::toEntity,
        )

    override fun observeContinueWatching(): Flow<List<ContinueWatching>> =
        localContinueWatchingDataSource
            .observeContinueWatching(1) // TODO : Add Authentication here
            .map(List<ContinueWatchingDto>::toEntities)

    override suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: ContinueWatching.ContentType,
    ) {
        val continueWatching =
            ContinueWatching(
                contentId = contentId,
                genreIds = genreIds,
                contentImageUrl = contentImageUrl,
                contentType = contentType,
                userId = 1, // TODO : Add Authentication here
            )
        executeSafely {
            localContinueWatchingDataSource.addContinueWatching(continueWatching.toDto())
        }
    }
}
