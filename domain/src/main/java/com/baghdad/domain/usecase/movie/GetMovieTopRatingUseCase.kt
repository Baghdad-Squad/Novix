package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie

class GetMovieTopRatingUseCase(
    private val topRatingRepository: MovieRepository,
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long?,
    ): PagedResult<Movie> {
        val result = topRatingRepository.getTopRatedMovies(page)
        val filteredMovies =
            if (genreId != null) {
                result.data.filter { movie ->
                movie.genres.any { genre -> genre.id == genreId }
            }
        } else {
            result.data
        }
        return result.copy(data = filteredMovies)
    }

}