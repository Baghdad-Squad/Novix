package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class GetMovieGalleryUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Long): List<String> {
        return movieRepository.getMovieImages(movieId)
    }

}