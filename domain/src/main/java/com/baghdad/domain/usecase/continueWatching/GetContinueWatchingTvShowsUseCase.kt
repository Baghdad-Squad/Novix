package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetContinueWatchingTvShowsUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): PagedResult<ContinueWatching> {
        return continueWatchingRepository.getPagedTvShows(page = page, pageSize = pageSize)
    }
}