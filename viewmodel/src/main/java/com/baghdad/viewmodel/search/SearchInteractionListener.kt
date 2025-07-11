package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentWatchingClick()
    fun onClearRecentSearchClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(searchText: String)
    fun onMovieCategoryClick()
    fun onTvShowCategoryClick()
    fun onActorCategoryClick()
    fun onBottomSheetCloseClick()
    fun onBottomSheetClearClick()
    fun onApplyClick()
    fun onClearFilterClick()
    fun onFilterIconClick()
    fun onRatingChanged(rating: Int)
    fun onYearRangeSelected(newRange: ClosedFloatingPointRange<Float>)
    fun onGenresSelected(genres: GenreUiState)
}
