package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long) = movieRepository.getMovieDetails(movieId)
}