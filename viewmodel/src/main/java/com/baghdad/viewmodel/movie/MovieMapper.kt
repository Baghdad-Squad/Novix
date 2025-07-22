package com.baghdad.viewmodel.movie

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie

fun Movie.toMovieUi() = MovieScreenState.MovieUiState(
    id = id,
    posterPictureURL = posterImageURL
)

fun Genre.toGenreUi() = MovieScreenState.CategoryUiState(
    id = id,
    name = name,
    isSelected = false
)