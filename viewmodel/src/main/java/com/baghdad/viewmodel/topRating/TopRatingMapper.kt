package com.baghdad.viewmodel.topRating

import com.baghdad.domain.model.search.SearchFilter
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.viewmodel.search.SearchScreenState
import com.baghdad.viewmodel.search.toGenre

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
    selectedGenres = selectedGenres.map { it.toGenre() }
)
