package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class GetPopularMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke() = movieRepository.getPopularMovies()
}