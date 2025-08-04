package com.baghdad.ui.navigation.graph.actorDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.actorDetails.ActorDetailsScreen
import com.baghdad.ui.feature.actorGallery.GalleryScreen
import com.baghdad.ui.feature.topMoviePicks.TopMoviePicksScreen
import com.baghdad.ui.feature.topTvShowPicks.TopTvShowPicksScreen
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
            ActorDetailsScreen { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
        composable<ActorDetailsRoute.ActorGalleryScreen> { backStackEntry ->
            GalleryScreen { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
        composable<ActorDetailsRoute.ActorTopMoviePicksScreen> { backStackEntry ->
            TopMoviePicksScreen { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
        composable<ActorDetailsRoute.ActorTopTvShowPicksScreen> { backStackEntry ->
            TopTvShowPicksScreen { event ->
                handleActorDetailsNavigation(event, navController)
            }
        }
    }
}

private fun handleActorDetailsNavigation(
    event: ActorDetailsNavEvent,
    navController: NavHostController,
) {
    when (event) {
        ActorDetailsNavEvent.NavigateBack -> navController.popBackStack()
        is ActorDetailsNavEvent.NavigateToActorGallery -> navController.navigate(
            route = ActorDetailsRoute.ActorGalleryScreen(event.actorId)
        )

        is ActorDetailsNavEvent.NavigateToActorTopMoviePicks -> navController.navigate(
            route = ActorDetailsRoute.ActorTopMoviePicksScreen(event.actorId)
        )

        is ActorDetailsNavEvent.NavigateToActorTopTvShowPicks -> navController.navigate(
            route = ActorDetailsRoute.ActorTopTvShowPicksScreen(actorId = event.actorId)
        )

        is ActorDetailsNavEvent.NavigateToMovieDetails -> navController.navigate(
            route =  MovieDetailsGraph(event.movieId)
        )

        is ActorDetailsNavEvent.NavigateToTvShowDetails -> navController.navigate(
            route = TvShowDetailsGraph(event.tvShowId)
        )

        ActorDetailsNavEvent.NavigateToLogin -> navController.navigate(AuthenticationGraph)

    }
}