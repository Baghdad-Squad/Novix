package com.baghdad.viewmodel.home

import com.baghdad.viewmodel.home.HomeScreenState.ContinueWatchingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.TopRatingItemUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState

interface HomeInteractionListener {
    fun onPopularItemClicked(item: PopularItemUiState)
    fun onPopularItemSaveClicked(item: PopularItemUiState)
    fun onMoviesClicked()
    fun onTvShowsClicked()
    fun onActorsClicked()
    fun onTopRatingItemClicked(item: TopRatingItemUiState)
    fun onTopRatingItemSaveClicked(item: TopRatingItemUiState)
    fun onViewAllTopRatingClicked()
    fun onContinueWatchingItemClicked(item: ContinueWatchingItemUiState)
    fun onContinueWatchingItemSaveClicked(item: ContinueWatchingItemUiState)
    fun onViewAllContinueWatchingClicked()
    fun onUpcomingGenreSelected(genre: GenreUiState?)
    fun onUpcomingItemClicked(item: UpcomingItemUiState)
    fun onUpcomingItemSaveClicked(item: UpcomingItemUiState)
    fun onSnackBarActionLabelClick()
}