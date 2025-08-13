package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetAllContinueWatchingUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): PagedResult<UserWatchedMedia> {
        return continueWatchingRepository.getContinueWatching(page = page, pageSize = pageSize)
    }
}