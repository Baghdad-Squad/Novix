package com.baghdad.viewmodel.movie

import com.baghdad.entity.media.Movie

fun Movie.toMovieUi() = TrendingMoviesScreenState.MovieUiState(
    id = id,
    posterPictureURL = posterImageURL
)