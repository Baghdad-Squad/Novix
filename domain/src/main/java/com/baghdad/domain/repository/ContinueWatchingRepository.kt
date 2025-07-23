package com.baghdad.domain.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult

interface ContinueWatchingRepository {
    suspend fun getContinueWatching(page: Int, pageSize: Int): PagedResult<ContinueWatching>
    suspend fun addContinueWatching(
        contentId: Long,
        genreIds: List<Long>,
        contentImageUrl: String,
        contentType: ContinueWatching.ContentType
    )
}