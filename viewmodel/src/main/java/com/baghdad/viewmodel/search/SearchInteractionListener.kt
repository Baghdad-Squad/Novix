package com.baghdad.viewmodel.search

import com.baghdad.entity.media.Genre

interface SearchInteractionListener {
    fun onSearchTextChanged(text: String)
    fun onClearRecentWatchingClick()
    fun onClearRecentSearchClick()
    fun onRemoveRecentSearchClick(id: Long)
    fun onRecentSearchItemClick(searchText: String)
    fun onMovieCategoryClick()
    fun onTvShowCategoryClick()
    fun onActorCategoryClick()
    fun onBottomSheetCloseClick()
    fun onBottomSheetClearClick()
    fun onApplyClick()
    fun onFilterClick()
    fun onRatingChanged(rating: Int)
    fun onYearRangeSelected(minYear: Int?, maxYear: Int?)
    fun onGenresSelected(genres: List<Genre>)
}
