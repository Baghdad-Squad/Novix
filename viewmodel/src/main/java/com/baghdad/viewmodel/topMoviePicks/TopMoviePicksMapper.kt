package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.domain.model.savedList.SavedMovie

fun SavedMovie.toUIState() =
    TopMoviePicksState.MovieUiState(
        id = movie.id,
        posterPictureURL = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L
)
