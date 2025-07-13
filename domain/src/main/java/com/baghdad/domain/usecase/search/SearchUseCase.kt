package com.baghdad.domain.usecase.search

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.domain.model.search.SearchResult
import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow


class SearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String, filter: SearchFilter): SearchResult {
        return searchRepository.searchByName(query).filter(filter)
    }

    private fun SearchResult.filter(filter: SearchFilter) = SearchResult(
        movies = filterMovies(movies, filter),
        tvShows = filterTvShows(tvShows, filter),
        actors = actors
    )

    private fun filterMovies(movies: List<Movie>, filter: SearchFilter): List<Movie> {
        return movies.filter { movie ->
            matchesRatingFilter(movie.averageRating, filter.minimumRating) &&
                    matchesYearFilter(
                        movie.releaseDate.year,
                        filter.minimumYear,
                        filter.maximumYear
                    ) &&
                    matchesGenreFilter(movie.genres, filter.selectedGenres)
        }
    }

    private fun filterTvShows(tvShows: List<TvShow>, filter: SearchFilter): List<TvShow> {
        return tvShows.filter { show ->
            matchesRatingFilter(show.averageRating, filter.minimumRating) &&
                    matchesYearFilter(
                        show.releaseDate.year,
                        filter.minimumYear,
                        filter.maximumYear
                    ) &&
                    matchesGenreFilter(show.genres, filter.selectedGenres)
        }
    }

    private fun matchesGenreFilter(itemGenres: List<Genre>, selectedGenres: List<Genre>): Boolean {
        return selectedGenres.isEmpty() || itemGenres.any { selectedGenres.contains(it) }
    }

    private fun matchesYearFilter(releaseYear: Int, minimumYear: Int, maximumYear: Int): Boolean {
        return releaseYear in minimumYear..maximumYear
    }

    private fun matchesRatingFilter(rating: Double, minimumRating: Int): Boolean {
        return rating >= minimumRating
    }
}