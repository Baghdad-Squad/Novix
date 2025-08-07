package com.baghdad.domain.usecase.mediaRated

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.RatedMedia
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetUserRatedMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(page: Int, pageSize: Int): PagedResult<RatedMedia> {
        return movieRepository.getUserRatedMovies(page, pageSize)
    }
}