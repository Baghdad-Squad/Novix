package com.baghdad.domain.usecase.search

import android.util.Log
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.entity.media.Movie

class SearchMoviesUseCase(
    private val searchRepository: SearchRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
    private val filterHelper: SearchFilterHelper
) {
    suspend operator fun invoke(
        query: String,
        filter: SearchFilter,
        page: Int?
    ): PagedResult<Movie> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        return searchRepository.searchMoviesByName(query, page).let {
            it.copy(data = it.data.filter { movie ->
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
            ).let {
                Log.e("from domain", "searching for $it")
                it
            }
        }
    }
}
