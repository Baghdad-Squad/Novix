package com.baghdad.ui.navigation.graph.movieDetails

sealed interface MovieDetailsNavEvent {
    data object NavigateBack : MovieDetailsNavEvent
    data object NavigateToLogin : MovieDetailsNavEvent
    data class NavigateToReviews(val movieId: Long) : MovieDetailsNavEvent
    data class NavigateToActorDetails(val actorId: Long) : MovieDetailsNavEvent
    data class NavigateToMovieDetails(val movieId: Long) : MovieDetailsNavEvent
    data class NavigateToCategoryMovies(val categoryId: Long) : MovieDetailsNavEvent
}