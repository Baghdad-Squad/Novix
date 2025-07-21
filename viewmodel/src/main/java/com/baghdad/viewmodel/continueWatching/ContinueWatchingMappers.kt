package com.baghdad.viewmodel.continueWatching

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie

fun Genre.toContinueWatchingUiState() = ContinueWatchingState.GenreUiState(
    id = id,
    name = name,
)

fun Movie.toContinueWatchingUiState() = ContinueWatchingState.ContinueWatchingMovieUiState(
    id = id,
    posterPictureURL = posterImageURL,

)