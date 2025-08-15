package com.baghdad.domain.usecase.userWatchedMedia

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.UserWatchedMediaRepository
import com.baghdad.domain.util.filterByGenre
import javax.inject.Inject

class GetUserWatchedMediaTvShowsByGenreUseCase @Inject constructor(
    private val userWatchedMediaRepository: UserWatchedMediaRepository
) {
    suspend operator fun invoke(
        genreId: Long,
        page: Int,
        pageSize: Int
    ): PagedResult<UserWatchedMedia> {
        val result = userWatchedMediaRepository.getPagedTvShows(page = page, pageSize = pageSize)
        return result.copy(result.data.filterByGenre(genreId))
    }
}