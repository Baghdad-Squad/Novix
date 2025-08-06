package com.baghdad.viewmodel.topRating

interface TopRatingInteractionListener {
    fun onMovieDetailsClick(movieId: Long)
    fun onTvShowDetailsClick(tvShowId: Long)
    fun onGenreClick(genreId: Long?)
    fun onSaveMovieClick()
    fun onBackClick()
    fun onSelectedTab(selectedTab: TopRatingTab)
    fun onSnackBarActionLabelClick()
    fun onCreateNewListClick()
    fun onLoginClick()
    fun onSaveToListBottomSheetDismiss()
    fun onListSelected(listId: Long)
    fun onCreatedListNameChanged(name: String)
    fun onCreateListBottomSheetDismiss()
    fun onCreateListBottomSheetAddClick()
    fun onTopRatingItemSaveClick(item: TopRatingState.MovieUiState)
}