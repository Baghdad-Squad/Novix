package com.baghdad.viewmodel.movieDetails

import com.baghdad.entity.media.Genre

fun Genre.toMoviesDetailsUiState() = MovieDetailsState.GenreUiState(
    id = id,
    name = name
)