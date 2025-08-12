package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.ContinueWatchingRepository
import javax.inject.Inject

class GetContinueWatchingMoviesByGenreUseCase @Inject constructor(
    private val continueWatchingRepository: ContinueWatchingRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<ContinueWatching> {
        val result = continueWatchingRepository.getPagedMovies(page = page, pageSize = pageSize)
        return result.copy(result.data.filter { it.genreIds.contains(genreId) })
    }
}

