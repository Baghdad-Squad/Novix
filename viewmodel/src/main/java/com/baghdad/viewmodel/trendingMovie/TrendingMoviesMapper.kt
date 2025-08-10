package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.media.Genre

fun SavableMovie.toMovieUiState() =
    TrendingMoviesScreenState.TrendingMovieUiState(
        id = movie.id,
        posterPictureURL = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
)

fun Genre.toGenreUiState() = TrendingMoviesScreenState.TrendingCategoryUiState(
    id = id,
    name = name
)