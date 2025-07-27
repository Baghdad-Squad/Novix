package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.entity.media.TvShow
import javax.inject.Inject

class SearchTvShowsUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
    private val filterHelper: SearchFilterHelper
) {
    suspend operator fun invoke(
        query: String,
        filter: SearchFilter,
        page: Int
    ): PagedResult<TvShow> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        val searchResults = searchRepository.searchTvShowsByName(query, page)

        val filteredShows = searchResults.data
            .filter { show -> matchesAllFilters(show, filter) }
            .sortedByDescending { show -> calculateFavoriteGenreScore(show, favoriteGenres) }

        return searchResults.copy(data = filteredShows)
    }

    private fun matchesAllFilters(show: TvShow, filter: SearchFilter): Boolean {
        return filterHelper.matchesRatingFilter(show.averageRating, filter.minimumRating) &&
                filterHelper.matchesYearFilter(
                    show.releaseDate.year,
                    filter.minimumYear,
                    filter.maximumYear
                ) &&
                filterHelper.matchesGenreFilter(show.genres, filter.selectedGenres)
    }

    private fun calculateFavoriteGenreScore(show: TvShow, favoriteGenres: Map<String, Int>): Int {
        return show.genres.sumOf { genre -> favoriteGenres[genre.name] ?: 0 }
    }
}
