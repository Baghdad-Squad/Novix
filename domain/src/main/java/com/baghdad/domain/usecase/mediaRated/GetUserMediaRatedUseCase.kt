package com.baghdad.domain.usecase.mediaRated

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.userRating.RatedMedia
import javax.inject.Inject

class GetUserMediaRatedUseCase @Inject constructor(
    private val getUserRatedTvShowsUseCase: GetUserRatedTvShowsUseCase,
    private val getUserRatedMoviesUseCase: GetUserRatedMoviesUseCase
) {
    suspend operator fun invoke(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        val tvShows = getUserRatedTvShowsUseCase(page, pageSize / 2)
        val movies = getUserRatedMoviesUseCase(page, pageSize / 2)
        return movies.copy(data = (movies.data + tvShows.data))
    }

}