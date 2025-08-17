package com.baghdad.viewmodel.search


interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentSearchClick()
    fun onClearRecentlyViewedClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(id: Long)

    fun onSaveMovieClick(movie: SearchScreenState.MovieUiState)

    fun onSaveRecentlyViewedClick(recentlyViewed: SearchScreenState.RecentlyViewedUiState)
    fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab)
    fun onRecentlyViewedClick(id: Long, imageUrl: String)
    fun onMovieItemClick(contentId: Long, contentImageUrl: String)
    fun onTvShowItemClick(contentId: Long, contentImageUrl: String)
    fun onActorItemClick(id: Long)
    fun onSnackBarActionLabelClick()

    fun onSaveItemToListClicked()

    fun onCreateNewListClicked()

    fun onLoginClicked()

    fun onSaveToListBottomSheetDismiss()

    fun onListSelected(listId: Long)

    fun onCreatedListNameChanged(name: String)

    fun onCreateListBottomSheetDismiss()

    fun onCreateListBottomSheetAddClick()
}