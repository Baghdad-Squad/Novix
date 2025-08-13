package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject


class AddMovieRateUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Long, rating: Int) {
         movieRepository.addMovieRate(movieId = movieId, rating =  rating)
    }
}