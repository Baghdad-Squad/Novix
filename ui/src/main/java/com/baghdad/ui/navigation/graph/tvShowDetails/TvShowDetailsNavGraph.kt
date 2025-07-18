package com.baghdad.ui.navigation.graph.tvShowDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.Graph.ActorDetailsGraph
import com.baghdad.ui.navigation.route.TvShowDetailsRoute
import com.baghdad.ui.navigation.route.TvShowDetailsRoute.EpisodeDetailsScreen
import com.baghdad.viewmodel.review.ContentType

fun NavGraphBuilder.tvShowDetailsNavGraph(navController: NavHostController) {
    navigation<Graph.TvShowDetailsGraph>(
        startDestination = TvShowDetailsRoute.TvShowDetailsScreen
    ) {
        composable<TvShowDetailsRoute.TvShowDetailsScreen> { backStackEntry ->
            val tvShowId = backStackEntry.toGraph<Graph.TvShowDetailsGraph>(navController).tvShowId
            DummyScreen(title = "Tv Show Details Screen: $tvShowId")
        }
        composable<EpisodeDetailsScreen> { backStackEntry ->
            val tvShowId = backStackEntry.toGraph<Graph.TvShowDetailsGraph>(navController).tvShowId
            val seasonNumber = backStackEntry.toRoute<EpisodeDetailsScreen>().seasonNumber
            val episodeNumber = backStackEntry.toRoute<EpisodeDetailsScreen>().episodeNumber
            DummyScreen(title = "Episode Details Screen: $tvShowId, Season: $seasonNumber, Episode: $episodeNumber")
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
            EpisodeDetailsScreen(event.seasonNumber, event.episodeNumber)
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