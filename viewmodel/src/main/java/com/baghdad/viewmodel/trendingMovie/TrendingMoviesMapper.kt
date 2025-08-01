package com.baghdad.viewmodel.trendingMovie

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie

fun Movie.toMovieUiState() = TrendingMoviesScreenState.TrendingMovieUiState(
    id = id,
    posterPictureURL = posterImageURL
)

fun Genre.toGenreUiState() = TrendingMoviesScreenState.TrendingCategoryUiState(
    id = id,
    name = name
)