package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.entity.media.Movie

class GetMovieTopRatingUseCase(
    private val movieRepository: MovieRepository,
    private val filterHelper: SearchFilterHelper
) {
    suspend operator fun invoke(
        filter: SearchFilter,
        page: Int
    ): List<Movie> {
        val searchResults = movieRepository.getTopRatedMovies(page)

        val filteredMovies = searchResults
            .filter { movie -> matchesAllFilters(movie, filter) }

        return filteredMovies
    }

    private fun matchesAllFilters(movie: Movie, filter: SearchFilter): Boolean {
        return filterHelper.matchesRatingFilter(movie.averageRating, filter.minimumRating) &&
                filterHelper.matchesYearFilter(
                    movie.releaseDate.year,
                    filter.minimumYear,
                    filter.maximumYear
                ) &&
                filterHelper.matchesGenreFilter(movie.genres, filter.selectedGenres)
    }

}