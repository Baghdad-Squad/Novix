package com.baghdad.domain.usecase.genre

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre

class GetGenresUseCase(
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) {
    suspend fun getMovieGenres(): List<Genre> {
        return movieRepository.getGenres()
    }

    suspend fun getTvShowGenres(): List<Genre> {
        return tvShowRepository.getGenres()
    }
}