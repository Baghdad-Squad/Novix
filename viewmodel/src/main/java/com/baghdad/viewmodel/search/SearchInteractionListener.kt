package com.baghdad.viewmodel.search

import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentWatchingClick()
    fun onClearRecentSearchClick()
    fun onRemoveRecentSearchItemClick(id: Long)
    fun onRecentSearchItemClick(id: Long)
    fun onMovieCategoryClick()
    fun onTvShowCategoryClick()
    fun onActorCategoryClick()
    fun onBottomSheetCloseClick()
    fun onBottomSheetClearClick()
    fun onApplyClick()
    fun onFilterIconClick()
    fun onRatingChanged(rating: Int)
    fun onYearRangeSelected(minYear: Int, maxYear: Int)
    fun onGenresSelected(genres: List<GenreUiState>)
}
