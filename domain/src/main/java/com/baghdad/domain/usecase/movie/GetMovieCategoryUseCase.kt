package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import javax.inject.Inject

class GetMovieCategoryUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): List<Genre> {
        return movieRepository.getMovieDetails(movieId).movie.genres
    }
}