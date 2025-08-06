package com.baghdad.ui.navigation.graph.myAccount

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.myRating.MyRatingScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.MyAccountRoute

fun NavGraphBuilder.myAccountNavGraph(navController: NavHostController) {
    navigation<Graph.MyAccountGraph>(
        startDestination = MyAccountRoute.MyAccountScreen
    ) {
        composable<MyAccountRoute.MyAccountScreen> {
            DummyScreen("My Account Screen")
        }
        composable<MyAccountRoute.MyRatingsScreen> {
            MyRatingScreen()
        }
        composable<MyAccountRoute.WatchingHistoryScreen> {
            DummyScreen("Watching History Screen")
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
            MyAccountRoute.WatchingHistoryScreen
        )

        is MyAccountNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is MyAccountNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )
    }
}