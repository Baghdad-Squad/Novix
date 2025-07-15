package com.baghdad.ui.navigation.graph.actorDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.actorDetails.ActorDetailsScreen
import com.baghdad.ui.feature.topMoviePicks.TopMoviePicksScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.ActorDetailsRoute
import com.baghdad.ui.navigation.route.Graph.ActorDetailsGraph
import com.baghdad.ui.navigation.route.Graph.AuthenticationGraph
import com.baghdad.ui.navigation.route.Graph.MovieDetailsGraph
import com.baghdad.ui.navigation.route.Graph.TvShowDetailsGraph

fun NavGraphBuilder.actorDetailsNavGraph(navController: NavHostController) {
    navigation<ActorDetailsGraph>(
        startDestination = ActorDetailsRoute.ActorDetailsScreen
    ) {
        composable<ActorDetailsRoute.ActorDetailsScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<ActorDetailsGraph>(navController).actorId
            ActorDetailsScreen(actorId = actorId) { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
        composable<ActorDetailsRoute.ActorGalleryScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<ActorDetailsGraph>(navController).actorId
            DummyScreen("Actor Gallery Screen $actorId")
        }
        composable<ActorDetailsRoute.ActorTopMoviePicksScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<ActorDetailsGraph>(navController).actorId
            TopMoviePicksScreen(actorId = actorId) { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
        composable<ActorDetailsRoute.ActorTopTvShowPicksScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<ActorDetailsGraph>(navController).actorId
            DummyScreen("Actor Top TV Show Picks Screen $actorId")
        }
    }
}

private fun handleActorDetailsNavigation(
    event: ActorDetailsNavEvent,
    navController: NavHostController
) {
    when (event) {
        ActorDetailsNavEvent.NavigateBack -> navController.popBackStack()
        ActorDetailsNavEvent.NavigateToActorGallery -> navController.navigate(
            ActorDetailsRoute.ActorGalleryScreen
        )

        ActorDetailsNavEvent.NavigateToActorTopMoviePicks -> navController.navigate(
            ActorDetailsRoute.ActorTopMoviePicksScreen
        )

        ActorDetailsNavEvent.NavigateToActorTopTvShowPicks -> navController.navigate(
            ActorDetailsRoute.ActorTopTvShowPicksScreen
        )

        is ActorDetailsNavEvent.NavigateToMovieDetails -> navController.navigate(
            MovieDetailsGraph(event.movieId)
        )

        is ActorDetailsNavEvent.NavigateToTvShowDetails -> navController.navigate(
            TvShowDetailsGraph(event.tvShowId)
        )

        ActorDetailsNavEvent.NavigateToLogin -> navController.navigate(AuthenticationGraph)
    }
}