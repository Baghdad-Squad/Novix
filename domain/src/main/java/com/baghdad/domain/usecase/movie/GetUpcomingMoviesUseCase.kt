package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie

class GetUpcomingMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Long?): List<Movie> = movieRepository.getUpcomingMovies(genreId)
}
