package com.baghdad.ui.navigation.graph.categories

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.categories.CategoriesScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.AuthenticationRoute
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.CategoriesRoute.CategoryMoviesScreen
import com.baghdad.ui.navigation.route.CategoriesRoute.CategoryTvShowsScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.Graph.MovieDetailsGraph
import com.baghdad.ui.navigation.route.Graph.TvShowDetailsGraph
import com.baghdad.ui.navigation.route.HomeRoute

fun NavGraphBuilder.categoriesNavGraph(navController: NavHostController) {
    navigation<Graph.CategoriesGraph>(
        startDestination = CategoriesRoute.CategoriesScreen
    ) {
        composable<CategoriesRoute.CategoriesScreen> {
            CategoriesScreen(
                handleNavigation = { event ->
                    handleCategoriesNavEvent(
                        event,
                        navController
                    )
                }
            )
        }
        composable<CategoryMoviesScreen> { backStackEntry ->
            com.baghdad.ui.feature.categoryMovies.CategoryMoviesScreen{
                handleCategoriesNavEvent(
                    it,
                    navController
                )
            }
        }
        composable<CategoryTvShowsScreen> { backStackEntry ->
            com.baghdad.ui.feature.categoryTvShows.CategoryTvShowsScreen {
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
        CategoriesNavEvent.NavigateToLogin ->
            navController.navigate(AuthenticationRoute.LoginScreen) {
                popUpTo(HomeRoute.HomeScreen) {
                    inclusive = true
                }
        }
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