package com.baghdad.viewmodel.categoryMovies

interface CategoryMoviesInteractionListener {
    fun onBackClick()
    fun onMovieClicked(movieId: Long)
    fun onSnackBarActionLabelClick()
    fun onSaveItemToListClick()
    fun onMovieToListClick(item: CategoryMoviesState.MovieUiState)
    fun onCreateNewListClick()
    fun onLoginClick()
    fun onSaveToListBottomSheetDismiss()
    fun onListSelected(listId: Long)
    fun onCreatedListNameChanged(name: String)
    fun onCreateListBottomSheetDismiss()
    fun onCreateListBottomSheetAddClick()
}