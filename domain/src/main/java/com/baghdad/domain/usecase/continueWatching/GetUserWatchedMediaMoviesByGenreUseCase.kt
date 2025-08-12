package com.baghdad.domain.usecase.continueWatching

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.UserWatchedMediaRepository
import javax.inject.Inject

class GetUserWatchedMediaMoviesByGenreUseCase @Inject constructor(
    private val userWatchedMediaRepository: UserWatchedMediaRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<UserWatchedMedia> {
        val result = userWatchedMediaRepository.getPagedMovies(page = page, pageSize = pageSize)
        return result.copy(result.data.filter { it.genreIds.contains(genreId) })
    }
}

