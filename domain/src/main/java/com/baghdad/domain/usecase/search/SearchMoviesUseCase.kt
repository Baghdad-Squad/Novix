package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.repository.FavoriteGenreRepository
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.Movie
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val favoriteGenreRepository: FavoriteGenreRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int
    ): PagedResult<Movie> {
        val favoriteGenres = favoriteGenreRepository.getFavoriteGenres()
        val searchResults = searchRepository.searchMoviesByTitle(query, page)

        val filteredMovies = searchResults.data
            .sortedByDescending { movie -> calculateFavoriteGenreScore(movie, favoriteGenres) }

        return searchResults.copy(data = filteredMovies)
    }

    private fun calculateFavoriteGenreScore(movie: Movie, favoriteGenres: Map<String, Int>): Int {
        return movie.genres.sumOf { genre -> favoriteGenres[genre.name] ?: 0 }
    }
}
