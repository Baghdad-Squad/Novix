package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.domain.util.SearchFilterHelper
import com.baghdad.entity.media.TvShow

class SearchTvShowsUseCase(
    private val tvShowRepository: TvShowRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
    private val filterHelper: SearchFilterHelper
) {
    suspend operator fun invoke(query: String, filter: SearchFilter): List<TvShow> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        return tvShowRepository.searchTvShowsByName(query)
            .filter { show ->
                filterHelper.matchesRatingFilter(show.averageRating, filter.minimumRating) &&
                        filterHelper.matchesYearFilter(
                            show.releaseDate.year,
                            filter.minimumYear,
                            filter.maximumYear
                        ) &&
                        filterHelper.matchesGenreFilter(show.genres, filter.selectedGenres)
            }
            .sortedByDescending { show ->
                show.genres.sumOf { genre -> favoriteGenres[genre.name] ?: 0 }
            }
    }
}
