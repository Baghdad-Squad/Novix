package com.baghdad.viewmodel.trendingMovie

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre

fun SavedMovie.toMovieUiState() = TrendingMoviesScreenState.TrendingMovieUiState(
    id = movie.id,
    posterPictureURL = movie.posterImageURL,
    isSaved = isSaved,
    savedListId = listId ?: -1L,
)

fun Genre.toGenreUiState() = TrendingMoviesScreenState.TrendingCategoryUiState(
    id = id,
    name = name
)