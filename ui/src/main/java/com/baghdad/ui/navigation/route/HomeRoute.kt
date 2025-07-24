package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface HomeRoute : Route {
    @Serializable
    data object HomeScreen : HomeRoute

    @Serializable
    data object PopularMoviesScreen : HomeRoute

    @Serializable
    data object TopRatingMoviesScreen : HomeRoute

    @Serializable
    data object MoviesScreen : HomeRoute

    @Serializable
    data object TvShowsScreen : HomeRoute

    @Serializable
    data object TrendingActorsScreen : HomeRoute

    @Serializable
    data object ContinueWatchingScreen : HomeRoute
}