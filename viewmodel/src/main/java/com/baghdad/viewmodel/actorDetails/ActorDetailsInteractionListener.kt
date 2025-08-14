package com.baghdad.viewmodel.actorDetails

interface ActorDetailsInteractionListener {
    fun onBackIconClick()
    fun onReadMoreBiographyClick()
    fun onViewAllGalleryClick()
    fun onViewAllTopMoviesPicksClick()
    fun onViewAllTopTvShowsClick()
    fun onMovieCardClick(movieId: Long)
    fun onTvShowCardClick(tvShowId: Long)
    fun onSaveMovieClick(movie: ActorDetailsScreenState.MovieUiState)
    fun onSaveItemToListClicked()
    fun onSnackBarActionLabelClick()
    fun onCreateNewListClicked()
    fun onLoginClicked()
    fun onSaveToListBottomSheetDismiss()
    fun onListSelected(listId: Long)
    fun onCreatedListNameChanged(name: String)
    fun onCreateListBottomSheetDismiss()
    fun onCreateListBottomSheetAddClick()
    fun onGalleryImageClick(imageUrl: String)
    fun onImageDialogDismiss()
}