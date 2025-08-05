package com.baghdad.ui.navigation.graph.myAccount

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.profile.ProfileScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.HomeRoute
import com.baghdad.ui.navigation.route.MyAccountRoute

fun NavGraphBuilder.myAccountNavGraph(navController: NavHostController) {
    navigation<Graph.MyAccountGraph>(
        startDestination = MyAccountRoute.MyAccountScreen
    ) {
        composable<MyAccountRoute.MyAccountScreen> {
            ProfileScreen {
                handleMyAccountNavigation(it, navController)
            }
        }
        composable<MyAccountRoute.MyRatingsScreen> {
            DummyScreen("My Ratings Screen")
        }
    }
}

private fun handleMyAccountNavigation(
    event: MyAccountNavEvent,
    navController: NavHostController
) {
    when (event) {
        MyAccountNavEvent.NavigateBack -> navController.popBackStack()

        MyAccountNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)

        MyAccountNavEvent.NavigateToMyRatings -> navController.navigate(
            MyAccountRoute.MyRatingsScreen
        )

        MyAccountNavEvent.NavigateToWatchingHistory -> navController.navigate(
            HomeRoute.ContinueWatchingScreen
        )

        is MyAccountNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is MyAccountNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )
    }
}