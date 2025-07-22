package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie

class GetTrendingMoviesUseCase(
    private val moviesRepository: MovieRepository
) {
    suspend operator fun invoke(page: Int, genreId: Long = 0): List<Movie> {
        val movies = moviesRepository.getTrendingMovies(page)

        return if (genreId == 0L) {
            movies
        } else {
            movies.filter { it.genres.any { genre -> genre.id == genreId } }
        }
    }
}

