package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.entity.media.Movie

fun Movie.toUIState() = TopMoviePicksState.MovieUiState(
    id = id,
    posterPictureURL = posterImageURL,
)