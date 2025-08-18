package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long? = null,
    ): PagedResult<SavedMovie> {
        val pagedResult = movieRepository.getTrendingMovies(page)
        return pagedResult.copy(
            data = pagedResult.data.filterByGenre(genreId)
        )
    }

    private fun List<SavedMovie>.filterByGenre(genreId: Long?): List<SavedMovie> {
        return if (genreId != null) {
            filter { savableMovie ->
                savableMovie.movie.genres.any { genre -> genre.id == genreId }
            }
        } else {
            this
        }
    }
}