package com.baghdad.viewmodel.topRating

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie


fun Movie.toTopRatingUIState() = TopRatingMovieState.MovieUiState(
    id = id,
    posterPictureURL = posterImageURL,
)

fun Genre.toTopRatingUIState() = TopRatingMovieState.GenreUiState(
    id = id,
    name = name
)

fun TopRatingMovieState.GenreUiState.toGenre() = Genre(
    id = id,
    name = name
)

fun TopRatingMovieState.MovieFilter.copyForTopRating() = SearchFilter(
    minimumYear = minimumYear,
    maximumYear = maximumYear,
    minimumRating = minimumRating,
    selectedGenres = selectedGenres.takeIf { it.id != 0L }?.let { listOf(it.toGenre()) }
        ?: emptyList()
)
