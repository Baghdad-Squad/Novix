package com.baghdad.ui.navigation.graph.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.continueWatching.ContinueWatchingScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.HomeRoute

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation<Graph.HomeGraph>(
        startDestination = HomeRoute.HomeScreen
    ) {
        composable<HomeRoute.HomeScreen> {
            DummyScreen(title = "Home Screen")
        }
        composable<HomeRoute.PopularMoviesScreen> {
            DummyScreen(title = "Popular Movies Screen")
        }
        composable<HomeRoute.TopRatingMoviesScreen> {
            DummyScreen(title = "Top Rating Movies Screen")
        }
        composable<HomeRoute.ContinueWatchingScreen> {
            ContinueWatchingScreen(
                handleNavigation = { event ->
                    handleHomeNavigation(event, navController)
                }
            )
        }
        composable<HomeRoute.MoviesScreen> {
            DummyScreen(title = "Movies Screen")
        }
        composable<HomeRoute.TvShowsScreen> {
            DummyScreen(title = "TV Shows Screen")
        }
        composable<HomeRoute.ActorsScreen> {
            DummyScreen(title = "Actors Screen")
        }
    }
}

private fun handleHomeNavigation(
    event: HomeNavEvent,
    navController: NavController
) {
    when (event) {
        HomeNavEvent.NavigateBack -> navController.navigateUp()
        HomeNavEvent.NavigateToPopularMovies -> navController.navigate(HomeRoute.PopularMoviesScreen)
        HomeNavEvent.NavigateToTopRatingMovies -> navController.navigate(HomeRoute.TopRatingMoviesScreen)
        HomeNavEvent.NavigateToContinueWatching -> navController.navigate(HomeRoute.ContinueWatchingScreen)
        HomeNavEvent.NavigateToMovies -> navController.navigate(HomeRoute.MoviesScreen)
        HomeNavEvent.NavigateToTvShows -> navController.navigate(HomeRoute.TvShowsScreen)
        HomeNavEvent.NavigateToActors -> navController.navigate(HomeRoute.ActorsScreen)
        is HomeNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is HomeNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )

        is HomeNavEvent.NavigateToActorDetails -> navController.navigate(
            Graph.ActorDetailsGraph(event.actorId)
        )

        HomeNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)
    }
}