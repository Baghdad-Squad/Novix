package com.baghdad.viewmodel.trendingMovie

interface TrendingMoviesInteractionListener {
    fun onBackClicked()

    fun onMovieClicked(movieId: Long)

    fun onSaveMovieClick(movie: TrendingMoviesScreenState.TrendingMovieUiState)

    fun onCategoryClicked(categoryId: Long?)

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