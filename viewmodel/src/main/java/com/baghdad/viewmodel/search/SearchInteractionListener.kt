package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentWatchingClick()
    fun onClearRecentSearchClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(searchText: String)
    fun onBottomSheetCloseClick()
    fun onBottomSheetClearClick()
    fun onApplyClick()
    fun onFilterIconClick()
    fun onRatingChanged(rating: Int)
    fun onYearRangeSelected(range: ClosedFloatingPointRange<Float>)
    fun onGenreSelected(genre: GenreUiState)
}