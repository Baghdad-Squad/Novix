package com.baghdad.ui.navigation.graph.actorDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.actorGallery.GalleryScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.ActorDetailsRoute
import com.baghdad.ui.navigation.route.Graph

fun NavGraphBuilder.actorDetailsNavGraph(navController: NavHostController) {
    navigation<Graph.ActorDetailsGraph>(
        startDestination = ActorDetailsRoute.ActorDetailsScreen
    ) {
        composable<ActorDetailsRoute.ActorDetailsScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<Graph.ActorDetailsGraph>(navController).actorId
            DummyScreen("Actor Details Screen: $actorId")
        }
        composable<ActorDetailsRoute.ActorGalleryScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<Graph.ActorDetailsGraph>(navController).actorId
            GalleryScreen(actorId) { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
        composable<ActorDetailsRoute.ActorTopMoviePicksScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<Graph.ActorDetailsGraph>(navController).actorId
            DummyScreen("Actor Top Movie Picks Screen $actorId")
        }
        composable<ActorDetailsRoute.ActorTopTvShowPicksScreen> { backStackEntry ->
            val actorId = backStackEntry.toGraph<Graph.ActorDetailsGraph>(navController).actorId
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
            Graph.MovieDetailsGraph(event.movieId)
        )

        is ActorDetailsNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )
    }
}