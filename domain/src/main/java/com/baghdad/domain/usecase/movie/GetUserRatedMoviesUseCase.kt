package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie
import javax.inject.Inject

class GetUserRatedMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(page: Int, pageSize: Int): PagedResult<Movie> {
        return movieRepository.getUserRatedMovies(page, pageSize)
    }
}