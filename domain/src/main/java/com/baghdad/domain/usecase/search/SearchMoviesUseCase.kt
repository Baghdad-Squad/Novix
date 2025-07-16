package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.entity.media.Movie

class SearchMoviesUseCase(
    private val movieRepository: MovieRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
    private val filterHelper: SearchFilterHelper
) {
    suspend operator fun invoke(query: String, filter: SearchFilter): List<Movie> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        return movieRepository.searchMoviesByName(query)
            .filter { movie ->
                filterHelper.matchesRatingFilter(movie.averageRating, filter.minimumRating) &&
                        filterHelper.matchesYearFilter(
                            movie.releaseDate.year,
                            filter.minimumYear,
                            filter.maximumYear
                        ) &&
                        filterHelper.matchesGenreFilter(movie.genres, filter.selectedGenres)
            }
            .sortedByDescending { movie ->
                movie.genres.sumOf { genre -> favoriteGenres[genre.name] ?: 0 }
            }
    }
}
