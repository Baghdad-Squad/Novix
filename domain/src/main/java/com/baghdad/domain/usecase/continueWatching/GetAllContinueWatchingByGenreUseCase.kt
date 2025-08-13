package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetAllContinueWatchingByGenreUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int
    ): PagedResult<UserWatchedMedia> {
        val result = continueWatchingRepository.getContinueWatching(page, PAGE_SIZE)
        return result.copy(result.data.filter { it.genreIds.contains(genreId) })
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}