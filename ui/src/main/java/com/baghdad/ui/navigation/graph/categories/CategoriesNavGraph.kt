package com.baghdad.ui.navigation.graph.categories

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.CategoriesRoute.CategoryMoviesScreen
import com.baghdad.ui.navigation.route.CategoriesRoute.CategoryTvShowsScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.Graph.MovieDetailsGraph
import com.baghdad.ui.navigation.route.Graph.TvShowDetailsGraph

fun NavGraphBuilder.categoriesNavGraph(navController: NavHostController) {
    navigation<Graph.CategoriesGraph>(
        startDestination = CategoriesRoute.CategoriesScreen
    ) {
        composable<CategoriesRoute.CategoriesScreen> {
            DummyScreen("Categories Screen")
        }
        composable<CategoryMoviesScreen> { backStackEntry ->
            val categoryId =
                backStackEntry.toRoute<CategoryMoviesScreen>().categoryId
            com.baghdad.ui.feature.categoryMovies.CategoryMoviesScreen(categoryId = categoryId) {
                handleCategoriesNavEvent(
                    it,
                    navController
                )
            }
        }
        composable<CategoryTvShowsScreen> { backStackEntry ->
            val categoryId =
                backStackEntry.toRoute<CategoryTvShowsScreen>().categoryId
            com.baghdad.ui.feature.categoryTvShows.CategoryTvShowsScreen(categoryId = categoryId) {
                handleCategoriesNavEvent(
                    it,
                    navController
                )
            }
        }
    }
}

private fun handleCategoriesNavEvent(
    event: CategoriesNavEvent,
    navController: NavHostController
) {
    when (event) {
        CategoriesNavEvent.NavigateBack -> navController.popBackStack()
        CategoriesNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph)
        is CategoriesNavEvent.NavigateToCategoryMovies -> navController.navigate(
            CategoryMoviesScreen(event.categoryId)
        )

        is CategoriesNavEvent.NavigateToCategoryTvShows -> navController.navigate(
            CategoryTvShowsScreen(event.categoryId)
        )

        is CategoriesNavEvent.NavigateToMovieDetails -> navController.navigate(
            MovieDetailsGraph(event.movieId)
        )

        is CategoriesNavEvent.NavigateToTvShowDetails -> navController.navigate(
            TvShowDetailsGraph(event.tvShowId)
        )

    }
}