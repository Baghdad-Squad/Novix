package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository

class GetMoviesByGenreUseCase(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Long, page: Int) = movieRepository.getMoviesByGenre(genreId, page)
}