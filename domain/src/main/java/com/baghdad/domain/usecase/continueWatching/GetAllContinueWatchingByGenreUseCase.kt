package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository

class GetAllContinueWatchingByGenreUseCase(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        userId: Long,
        genreId: Long,
        page: Int
    ): PagedResult<ContinueWatching> {
        return continueWatchingRepository.getMoviesByGenreId(userId, genreId, page, PAGE_SIZE)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}