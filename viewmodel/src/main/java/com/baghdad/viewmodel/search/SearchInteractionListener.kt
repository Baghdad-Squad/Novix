package com.baghdad.viewmodel.search


interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentSearchClick()
    fun onClearRecentlyViewedClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(id: Long)
    fun onSaveRecentlyViewedClick(id: Long)
    fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab)
    fun onRecentlyViewedClick(id: Long, imageUrl: String)
    fun onMovieItemClick(contentId: Long, contentImageUrl: String)
    fun onTvShowItemClick(contentId: Long, contentImageUrl: String)
    fun onActorItemClick(id: Long)
    fun onSnackBarActionLabelClick()
}
