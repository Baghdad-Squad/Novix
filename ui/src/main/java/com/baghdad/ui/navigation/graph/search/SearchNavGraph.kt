package com.baghdad.ui.navigation.graph.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.search.SearchScreen
import com.baghdad.ui.navigation.route.AuthenticationRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.HomeRoute
import com.baghdad.ui.navigation.route.SearchRoute

fun NavGraphBuilder.searchNavGraph(navController: NavHostController) {
    navigation<Graph.SearchGraph>(
        startDestination = SearchRoute.SearchScreen
    ) {
        composable<SearchRoute.SearchScreen> {
            SearchScreen { event ->
                handleSearchNavigation(event, navController)
            }
        }
    }
}

private fun handleSearchNavigation(
    event: SearchNavEvent,
    navController: NavHostController
) {
    when (event) {
        is SearchNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is SearchNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )

        is SearchNavEvent.NavigateToActorDetails -> navController.navigate(
            Graph.ActorDetailsGraph(event.actorId)
        )

        SearchNavEvent.NavigateToLogin ->
            navController.navigate(AuthenticationRoute.LoginScreen) {
                popUpTo(HomeRoute.HomeScreen) {
                    inclusive = true
                }
        }
    }
}