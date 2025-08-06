package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksState.MovieUiState

interface TopMoviePicksInteractionListener {
    fun onMovieDetailsClicked(movieId: Long)

    fun onSaveMovieClicked(item: MovieUiState)

    fun onBackClicked()

    fun onSnackBarActionLabelClicked()

    fun onSaveItemToListClicked()

    fun onCreateNewListClicked()

    fun onLoginClicked()

    fun onSaveToListBottomSheetDismiss()

    fun onListSelected(listId: Long)

    fun onCreatedListNameChanged(name: String)

    fun onCreateListBottomSheetDismiss()

    fun onCreateListBottomSheetAddClick()
}