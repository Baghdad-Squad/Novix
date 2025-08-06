package com.baghdad.domain.usecase

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.usecase.movie.GetUserRatedMoviesUseCase
import com.baghdad.domain.usecase.tvShow.GetUserRatedTvShowsUseCase
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
