package com.baghdad.domain.usecase.mediaRated

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.repository.TvShowRepository
import javax.inject.Inject

class GetUserRatedTvShowsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        return tvShowRepository.getUserRatedTvShows(page, pageSize)
    }
}