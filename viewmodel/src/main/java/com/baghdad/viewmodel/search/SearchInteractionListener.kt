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
    fun onRecentlyViewedClick(id: Long)
    fun onMovieItemClick(contentId: Long)
    fun onTvShowItemClick(contentId: Long)
    fun onActorItemClick(id: Long)
}
