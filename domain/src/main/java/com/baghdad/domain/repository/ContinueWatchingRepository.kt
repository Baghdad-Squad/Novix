package com.baghdad.domain.repository

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult

interface ContinueWatchingRepository {
    suspend fun getContinueWatching(userId: Long, page: Int, pageSize: Int): PagedResult<ContinueWatching>
    suspend fun addContinueWatching(continueWatching: ContinueWatching)
}