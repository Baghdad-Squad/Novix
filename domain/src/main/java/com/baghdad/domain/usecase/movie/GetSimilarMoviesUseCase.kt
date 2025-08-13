package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): List<SavedMovie> {
        return movieRepository.getSimilarMovies(movieId = movieId)
    }
}