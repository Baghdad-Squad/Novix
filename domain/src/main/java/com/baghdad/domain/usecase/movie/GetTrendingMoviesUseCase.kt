package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.domain.repository.MovieRepository
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        page: Int,
        genreId: Long? = null,
    ): PagedResult<SavableMovie> {
        val pagedResult = movieRepository.getTrendingMovies(page)
        if (genreId == null) return pagedResult

        val filteredData =
            pagedResult.data.filter { savableMovie ->
                savableMovie.movie.genres.any { genre -> genre.id == genreId }
        }

        return PagedResult(
            data = filteredData, nextKey = pagedResult.nextKey, prevKey = pagedResult.prevKey
        )
    }
}
