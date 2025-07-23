package com.baghdad.domain.usecase.continueWatching

import android.util.Log
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
        val result = continueWatchingRepository.getContinueWatching(userId, page, PAGE_SIZE)
        return result.copy(result.data.filter { it.genreIds.contains(genreId) })
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}