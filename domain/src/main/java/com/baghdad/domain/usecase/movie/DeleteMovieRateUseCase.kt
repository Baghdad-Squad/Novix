package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class DeleteMovieRateUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Long) {
        movieRepository.deleteMovieRate(movieId)
    }
}