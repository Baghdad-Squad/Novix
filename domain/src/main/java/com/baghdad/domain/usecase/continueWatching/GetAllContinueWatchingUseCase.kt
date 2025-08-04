package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetAllContinueWatchingUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        page: Int,
    ): PagedResult<ContinueWatching> {
        return continueWatchingRepository.getContinueWatching(page, PAGE_SIZE)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}