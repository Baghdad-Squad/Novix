package com.baghdad.viewmodel.movie

import com.baghdad.entity.media.Movie

fun Movie.toMovieUiState() = TrendingMoviesScreenState.TrendingMovieUiState(
    id = id,
    posterPictureURL = posterImageURL
)