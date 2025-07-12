package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentSearchClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(id: Long)
    fun onBottomSheetCloseClick()
    fun onBottomSheetClearClick()
    fun onApplyClick()
    fun onFilterIconClick()
    fun onRatingChanged(rating: Int)
    fun onYearRangeSelected(range: ClosedFloatingPointRange<Float>)
    fun onGenreSelected(genre: GenreUiState)
    fun onClearRecentlyViewedClick()
    fun onSavedRecentlyViewedClick(id: Long)
    fun onSelectedSearchTabChanged(selectedTab: SearchTab)
    fun onRecentlyViewedClick(id: Long)
}
