package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class GetSimilarMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long) = movieRepository.getSimilarMovies(movieId = movieId)
}