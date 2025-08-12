package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetContinueWatchingTvShowsByGenreUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<UserWatchedMedia> {
        val result = continueWatchingRepository.getPagedTvShows(page = page, pageSize = pageSize)
        return result.copy(result.data.filter { it.genreIds.contains(genreId) })
    }
}