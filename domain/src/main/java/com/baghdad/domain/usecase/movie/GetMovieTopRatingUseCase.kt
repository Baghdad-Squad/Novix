package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieTopRatingUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long?,
    ): PagedResult<SavedMovie> {
        val result = movieRepository.getTopRatedMovies(page = page)
        return result.copy(
            data = result.data.filterByGenre(genreId)
        )
    }

    private fun List<SavedMovie>.filterByGenre(
        genreId: Long?
    ): List<SavedMovie> {
        return if (genreId != null) {
            filter { savableMovie ->
                savableMovie.movie.genres.any { genre -> genre.id == genreId }
            }
        } else {
            this
        }
    }
}