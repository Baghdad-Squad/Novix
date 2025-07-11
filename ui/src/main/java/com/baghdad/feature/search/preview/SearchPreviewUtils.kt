package com.baghdad.feature.search.preview

import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState

val DummySearchListener = object : SearchInteractionListener {
    override fun onSearchTextChanged(text: String) {}
    override fun onClearRecentWatchingClick() {}
    override fun onClearRecentSearchClick() {}
    override fun onRemoveRecentSearchItemClick(id: Long) {}
    override fun onRecentSearchItemClick(searchText: String) {}
    override fun onMovieCategoryClick() {}
    override fun onTvShowCategoryClick() {}
    override fun onActorCategoryClick() {}
    override fun onBottomSheetCloseClick() {

    }

    override fun onBottomSheetClearClick() {}
    override fun onApplyClick() {}
    override fun onClearFilterClick() {}
    override fun onFilterIconClick() {}
    override fun onRatingChanged(rating: Int) {}
    override fun onYearRangeSelected(newRange: ClosedFloatingPointRange<Float>) {}
    override fun onGenresSelected(genres: SearchScreenState.GenreUiState) {}
}