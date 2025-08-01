package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentSearchClick()
    fun onClearRecentlyViewedClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(id: Long)
    fun onFilterCloseIconClick()
    fun onFilterClearClick()
    fun onApplyFilterClick()
    fun onFilterIconClick()
    fun onRatingChanged(rating: Int)
    fun onYearRangeSelected(range: ClosedFloatingPointRange<Float>)
    fun onGenreSelected(genre: GenreUiState)
    fun onSaveRecentlyViewedClick(id: Long)
    fun onSelectedSearchTabChanged(selectedTab: SearchScreenState.SearchTab)
    fun onRecentlyViewedClick(id: Long, imageUrl: String)
    fun onMovieItemClick(contentId: Long, contentImageUrl: String)
    fun onTvShowItemClick(contentId: Long, contentImageUrl: String)
    fun onActorItemClick(id: Long)

    fun onSnackBarActionLabelClick()

    fun onReloadFilterGenres()
}
