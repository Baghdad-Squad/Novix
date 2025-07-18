package com.baghdad.ui.navigation.graph.movieDetails

import androidx.navigation.NavGraphBuilder
import com.baghdad.ui.feature.movieDetails.MovieDetailsScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.MovieDetailsRoute
import com.baghdad.viewmodel.review.ContentType

fun NavGraphBuilder.movieDetailsNavGraph(navController: NavHostController) {
    navigation<Graph.MovieDetailsGraph>(
        startDestination = MovieDetailsRoute.MovieDetailsScreen
    ) {
        composable<MovieDetailsRoute.MovieDetailsScreen> { backStackEntry ->
            val movieId = backStackEntry.toGraph<Graph.MovieDetailsGraph>(navController).movieId
            MovieDetailsScreen (
                movieId  = movieId,
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
            Graph.ReviewsGraph(event.movieId, ContentType.MOVIE)
        )
    }
}