package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.entity.media.Movie
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
    private val filterHelper: SearchFilterHelper
) {
    suspend operator fun invoke(
        query: String,
        filter: SearchFilter,
        page: Int
    ): PagedResult<Movie> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        val searchResults = searchRepository.searchMoviesByTitle(query, page)

        val filteredMovies = searchResults.data
            .filter { movie -> matchesAllFilters(movie, filter) }
            .sortedByDescending { movie -> calculateFavoriteGenreScore(movie, favoriteGenres) }

        return searchResults.copy(data = filteredMovies)
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

    private fun calculateFavoriteGenreScore(movie: Movie, favoriteGenres: Map<String, Int>): Int {
        return movie.genres.sumOf { genre -> favoriteGenres[genre.name] ?: 0 }
    }
}
