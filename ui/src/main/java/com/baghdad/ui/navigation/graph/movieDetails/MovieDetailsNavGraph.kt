package com.baghdad.ui.navigation.graph.movieDetails

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.movieDetails.MovieDetailsScreen
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.MovieDetailsRoute
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.movieDetailsNavGraph(navController: NavHostController) {
    navigation<Graph.MovieDetailsGraph>(
        startDestination = MovieDetailsRoute.MovieDetailsScreen
    ) {
        composable<MovieDetailsRoute.MovieDetailsScreen> { backStackEntry ->
            val movieId = backStackEntry.toGraph<Graph.MovieDetailsGraph>(navController).movieId
            val viewModel : MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) })
            val state by viewModel.uiState.collectAsState()
            val snakBar by viewModel.snackBarState.collectAsStateWithLifecycle()
            MovieDetailsScreen(
                state = state,
                listener = viewModel,
                viewModel  = viewModel,
                snakBarState = snakBar,
                handleNavigation = { event ->
                    handleMovieDetailsNavigation(event, navController)
                }
            )
    }
    }
}

private fun handleMovieDetailsNavigation(
    event: MovieDetailsNavEvent,
    navController: NavHostController
) {
    when (event) {
        MovieDetailsNavEvent.NavigateBack -> navController.popBackStack()

        MovieDetailsNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)

        is MovieDetailsNavEvent.NavigateToCategoryMovies -> navController.navigate(
            CategoriesRoute.CategoryMoviesScreen(event.categoryId)
        )

        is MovieDetailsNavEvent.NavigateToActorDetails -> navController.navigate(
            Graph.ActorDetailsGraph(event.actorId)
        )

        is MovieDetailsNavEvent.NavigateToMovieDetails -> navController.navigate(
            Graph.MovieDetailsGraph(event.movieId)
        )

        is MovieDetailsNavEvent.NavigateToReviews -> navController.navigate(
            Graph.ReviewsGraph(event.movieId, "movie")
        )
    }
}