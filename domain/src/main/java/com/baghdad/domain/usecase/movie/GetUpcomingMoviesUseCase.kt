package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie

class GetUpcomingMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int, genreId: Long?): PagedResult<Movie> {
        return movieRepository.getUpcomingMovies(page, 20, genreId)
    }
}