package com.baghdad.viewmodel.continueWatching

interface ContinueWatchingInteractionListener {
    fun onBackClick()
    fun onMediaClick(mediaId: Long, contentType : ContinueWatchingState.ContinueWatchingMovieUiState.ContentType)
    fun onGenreClick(genreId: Long?)
    fun onSelectedTab(isMovieTab: Boolean)

    fun onMovieSaveClick(movie: ContinueWatchingState.ContinueWatchingMovieUiState)

    fun onSnackBarActionClick()

    fun onSaveItemToListClicked()

    fun onCreateNewListClicked()

    fun onLoginClicked()

    fun onSaveToListBottomSheetDismiss()

    fun onListSelected(listId: Long)

    fun onCreatedListNameChanged(name: String)

    fun onCreateListBottomSheetDismiss()

    fun onCreateListBottomSheetAddClick()
}