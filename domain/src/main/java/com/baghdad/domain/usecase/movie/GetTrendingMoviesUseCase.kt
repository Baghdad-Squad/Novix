package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.TrendingMovieRepository
import com.baghdad.entity.media.Movie

class GetTrendingMoviesUseCase(
    private val trendingMovieRepository: TrendingMovieRepository
) {

    suspend operator fun invoke(page: Int, genreId: Long = 0L): PagedResult<Movie> {
        val pagedResult = trendingMovieRepository.getTrendingMovies(page)

        return if (genreId != 0L) {
            val filteredData = pagedResult.data.filter { movie ->
                movie.genres.any { genre -> genre.id == genreId }
            }
            PagedResult(
                data = filteredData,
                nextKey = pagedResult.nextKey,
                prevKey = pagedResult.prevKey
            )
        } else {
            pagedResult
        }
    }
}
