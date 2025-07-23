package com.baghdad.domain.usecase.movie

import android.util.Log
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TopRatingRepository
import com.baghdad.entity.media.Movie

class GetMovieTopRatingUseCase(
    private val topRatingRepository: TopRatingRepository,
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long
    ): PagedResult<Movie> {
        val result = topRatingRepository.getTopRatedMovies(page)

        val filteredMovies = if (genreId != 0L) {
            result.data.filter { movie ->
                movie.genres.any { genre -> genre.id == genreId }
            }
        } else {
            result.data
        }

        Log.d(
            "GetMovieTopRatingUseCase", "Filtered ${
                filteredMovies.map {
                    it.genres.size
                }
            }"
        )
        Log.d("GetMovieTopRatingUseCase", "Filtered Id is $genreId")

        return result.copy(data = filteredMovies)
    }

}