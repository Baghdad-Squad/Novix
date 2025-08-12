package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetContinueWatchingMoviesUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): PagedResult<ContinueWatching> {
        return continueWatchingRepository.getPagedMovies(page = page, pageSize = pageSize)
    }
}


