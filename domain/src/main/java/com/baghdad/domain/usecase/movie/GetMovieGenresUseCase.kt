package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend fun getMovieGenres(): List<Genre> {
        return movieRepository.getGenres()
    }
}