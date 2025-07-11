package com.baghdad.domain.usecase.genre

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Genre

class GetGenresUseCase(
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(): List<Genre> {
        val movieGenres = movieRepository.getGenres()
        val tvShowGenres = tvShowRepository.getGenres()
        return (movieGenres + tvShowGenres).distinctBy { it.id }
    }
}