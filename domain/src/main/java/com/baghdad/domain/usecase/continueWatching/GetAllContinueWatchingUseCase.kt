package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository

class GetAllContinueWatchingUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        userId: Long,
        page: Int,
    ): PagedResult<ContinueWatching> {
        return continueWatchingRepository.getContinueWatching(userId, page, PAGE_SIZE)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}