package com.baghdad.domain.usecase.movieDetails

import com.baghdad.domain.repository.MovieRepository

class GetMovieGalleryUseCase(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(movieId: Long): List<String> {
        return listOf(movieRepository.getMovieDetails(movieId).posterImageURL)
    }

}