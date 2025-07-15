package com.baghdad.ui.navigation.graph.categories

sealed interface CategoriesNavEvent {
    data object NavigateBack : CategoriesNavEvent
    data object NavigateToLogin : CategoriesNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : CategoriesNavEvent
    data class NavigateToTvShowDetails(val tvShowId: Long) : CategoriesNavEvent
    data class NavigateToCategoryMovies(val categoryId: Long) : CategoriesNavEvent
    data class NavigateToCategoryTvShows(val categoryId: Long) : CategoriesNavEvent
}