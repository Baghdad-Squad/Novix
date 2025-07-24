package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie

class GetMoviesByGenreUseCase(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Long, page: Int): PagedResult<Movie> {
        return movieRepository.getMoviesByGenre(genreId, page, PAGE_SIZE)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}