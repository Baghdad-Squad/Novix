package com.baghdad.viewmodel.home

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class HomeViewModel(
) : BaseViewModel<HomeScreenState, HomeScreenEffect>(HomeScreenState()), HomeInteractionListener {
    init {
        getPopularItems()
        getTopRatingMovies()
//        getContinueWatchingItems()
//        getMovieGenres()
//        getUpcomingMovies()
    }

    private fun getTopRatingMovies() {
        TODO("Not yet implemented")
    }

    private fun getPopularItems() {
        TODO("Not yet implemented")
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }

    override fun onPopularItemClicked(item: HomeScreenState.PopularItemUiState) {
        TODO("Not yet implemented")
    }

    override fun onPopularItemSaveClicked(item: HomeScreenState.PopularItemUiState) {
        TODO("Not yet implemented")
    }

    override fun onTopRatingItemClicked(item: HomeScreenState.TopRatingItemUiState) {
        sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
    }

    override fun onTopRatingItemSaveClicked(item: HomeScreenState.TopRatingItemUiState) {
        TODO("Not yet implemented")
    }

    override fun onViewAllTopRatingClicked() {
        TODO("Not yet implemented")
    }

    override fun onContinueWatchingItemClicked(item: HomeScreenState.ContinueWatchingItemUiState) {
        sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
    }

    override fun onContinueWatchingItemSaveClicked(item: HomeScreenState.ContinueWatchingItemUiState) {
        TODO("Not yet implemented")
    }

    override fun onViewAllContinueWatchingClicked() {
        TODO("Not yet implemented")
    }

    override fun onUpcomingGenreSelected(genre: HomeScreenState.GenreUiState) {
        TODO("Not yet implemented")
    }

    override fun onUpcomingItemClicked(item: HomeScreenState.UpcomingItemUiState) {
        sendEffect(HomeScreenEffect.NavigateToMovieDetails(item.id))
    }

    override fun onUpcomingItemSaveClicked(item: HomeScreenState.UpcomingItemUiState) {
        TODO("Not yet implemented")
    }
}