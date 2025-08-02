package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.TvShow

class SearchTvShowsUseCase(
    private val searchRepository: SearchRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int
    ): PagedResult<TvShow> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        val searchResults = searchRepository.searchTvShowsByName(query, page)

        val filteredShows = searchResults.data
            .sortedByDescending { show -> calculateFavoriteGenreScore(show, favoriteGenres) }

        return searchResults.copy(data = filteredShows)
    }

    private fun calculateFavoriteGenreScore(show: TvShow, favoriteGenres: Map<String, Int>): Int {
        return show.genres.sumOf { genre -> favoriteGenres[genre.name] ?: 0 }
    }
}
