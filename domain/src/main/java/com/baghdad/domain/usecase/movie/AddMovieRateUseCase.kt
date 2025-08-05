package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class AddMovieRateUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Long, rating: Int) {
         movieRepository.addMovieRate(movieId, rating)
    }
}