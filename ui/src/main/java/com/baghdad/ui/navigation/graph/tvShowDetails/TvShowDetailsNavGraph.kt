package com.baghdad.ui.navigation.graph.tvShowDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.episodeDetails.EpisodeDetailsScreen
import com.baghdad.ui.feature.tvShowDetails.TvShowDetailsScreen
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.Graph.ActorDetailsGraph
import com.baghdad.ui.navigation.route.TvShowDetailsRoute
import com.baghdad.viewmodel.review.ContentType

fun NavGraphBuilder.tvShowDetailsNavGraph(navController: NavHostController) {
    navigation<Graph.TvShowDetailsGraph>(
        startDestination = TvShowDetailsRoute.TvShowDetailsScreen
    ) {
        composable<TvShowDetailsRoute.TvShowDetailsScreen> { backStackEntry ->
            val tvShowId = backStackEntry.toGraph<Graph.TvShowDetailsGraph>(navController).tvShowId
            TvShowDetailsScreen(tvShowId) { event ->
                handleTvShowDetailsNavEvent(event, navController)
            }
        }
        composable<TvShowDetailsRoute.EpisodeDetailsScreen> { backStackEntry ->
            EpisodeDetailsScreen{ navEvent ->
                handleTvShowDetailsNavEvent(navEvent, navController)
            }
        }
    }
}

private fun handleTvShowDetailsNavEvent(
    event: TvShowDetailsNavEvent,
    navController: NavHostController
) {
    when (event) {
        TvShowDetailsNavEvent.NavigateBack -> navController.popBackStack()

        is TvShowDetailsNavEvent.NavigateToEpisodeDetails -> navController.navigate(
            TvShowDetailsRoute.EpisodeDetailsScreen(event.seasonNumber, event.episodeNumber)
        )

        is TvShowDetailsNavEvent.NavigateToActorDetails -> navController.navigate(
            ActorDetailsGraph(event.actorId)
        )

        is TvShowDetailsNavEvent.NavigateToCategoryTvShows -> navController.navigate(
            CategoriesRoute.CategoryTvShowsScreen(event.categoryId)
        )

        TvShowDetailsNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)

        is TvShowDetailsNavEvent.NavigateToReviews -> navController.navigate(
            Graph.ReviewsGraph(event.movieId, ContentType.SERIES)
        )
    }
}