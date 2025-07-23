package com.baghdad.ui.navigation.graph.currentWatching

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.continueWatching.ContinueWatchingScreen
import com.baghdad.ui.navigation.route.ContinueWatchingRoute
import com.baghdad.ui.navigation.route.Graph

fun NavGraphBuilder.continueWatchingNavGraph(navController: NavHostController) {
    navigation<Graph.ContinueWatchingGraph>(
        startDestination = ContinueWatchingRoute.CurrentWatchingScreen
    ) {
        composable<ContinueWatchingRoute.CurrentWatchingScreen> {
            ContinueWatchingScreen(
                handleNavigation = { event ->
                    handleContinueWatchingNavigation(event, navController)
                }
            )
        }
    }
}

private fun handleContinueWatchingNavigation(
    event: ContinueWatchingNavEvent,
    navController: NavHostController
) {
    when (event) {
        ContinueWatchingNavEvent.NavigateBack -> navController.popBackStack()
        ContinueWatchingNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)
        is ContinueWatchingNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is ContinueWatchingNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )
    }

}
