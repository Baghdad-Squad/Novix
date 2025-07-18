package com.baghdad.ui.navigation.graph.reviews

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.review.ReviewScreen
import com.baghdad.ui.navigation.graph.util.toGraph
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.ReviewsRoute
import com.baghdad.viewmodel.review.ContentType
import kotlin.reflect.typeOf

fun NavGraphBuilder.reviewsNavGraph(navController: NavHostController) {
    navigation<Graph.ReviewsGraph>(
        startDestination = ReviewsRoute.ReviewsScreen,
        typeMap = mapOf(
            typeOf<ContentType>() to NavType.EnumType(ContentType::class.java)
        )
    ) {
        composable<ReviewsRoute.ReviewsScreen> { backStackEntry ->

        val graph = backStackEntry.toGraph<Graph.ReviewsGraph>(navController)

            ReviewScreen(
                mediaId = graph.mediaId,
                mediaType = graph.mediaType
            ) { event ->
                handleReviewsNavEvent(event = event, navController = navController)
            }
        }
    }
}

private fun handleReviewsNavEvent(
    event: ReviewsNavEvent,
    navController: NavHostController
) {
    when (event) {
        ReviewsNavEvent.NavigateBack -> navController.popBackStack()
    }
}