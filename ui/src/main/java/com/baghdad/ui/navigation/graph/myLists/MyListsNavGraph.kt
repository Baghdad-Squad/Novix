package com.baghdad.ui.navigation.graph.myLists

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.baghdad.ui.feature.myLists.MyListsScreen
import com.baghdad.ui.feature.savedListDetails.SavedListDetailsScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.MyListsRoute

fun NavGraphBuilder.myListsNavGraph(navController: NavHostController) {
    navigation<Graph.MyListsGraph>(
        startDestination = MyListsRoute.MyListsScreen
    ) {
        composable<MyListsRoute.MyListsScreen> {
            MyListsScreen {
                handleMyListsNavEvent(it, navController)
            }
        }
        composable<MyListsRoute.ListDetailsScreen> { backStackEntry ->
            SavedListDetailsScreen {
                handleMyListsNavEvent(it, navController)
            }
        }
    }
}

private fun handleMyListsNavEvent(
    event: MyListsNavEvent,
    navController: NavHostController
) {
    when (event) {
        is MyListsNavEvent.NavigateToListDetails -> navController.navigate(
            MyListsRoute.ListDetailsScreen(event.listId)
        )

        is MyListsNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is MyListsNavEvent.NavigateToTvShowDetails -> navController.navigate(
            Graph.TvShowDetailsGraph(event.tvShowId)
        )

        MyListsNavEvent.NavigateBack -> navController.popBackStack()

        MyListsNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)
    }
}