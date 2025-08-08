package com.baghdad.domain.usecase.topRated

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieTopRatingUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long?,
    ): PagedResult<SavableMovie> {
        val result = movieRepository.getTopRatedMovies(page)
        val filteredMovies = if (genreId != null) {
            result.data.filter { savableMovie ->
                savableMovie.movie.genres.any { genre -> genre.id == genreId }
            }
        } else {
            result.data
        }
        return result.copy(data = filteredMovies)
    }

}