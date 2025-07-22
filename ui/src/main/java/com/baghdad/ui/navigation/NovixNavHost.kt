package com.baghdad.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baghdad.ui.navigation.graph.actorDetails.actorDetailsNavGraph
import com.baghdad.ui.navigation.graph.authentication.authenticationNavGraph
import com.baghdad.ui.navigation.graph.categories.categoriesNavGraph
import com.baghdad.ui.navigation.graph.currentWatching.continueWatchingNavGraph
import com.baghdad.ui.navigation.graph.home.homeNavGraph
import com.baghdad.ui.navigation.graph.movieDetails.movieDetailsNavGraph
import com.baghdad.ui.navigation.graph.myAccount.myAccountNavGraph
import com.baghdad.ui.navigation.graph.myLists.myListsNavGraph
import com.baghdad.ui.navigation.graph.onBoarding.onBoardingNavGraph
import com.baghdad.ui.navigation.graph.reviews.reviewsNavGraph
import com.baghdad.ui.navigation.graph.search.searchNavGraph
import com.baghdad.ui.navigation.graph.tvShowDetails.tvShowDetailsNavGraph
import com.baghdad.ui.navigation.route.Route

@Composable
fun NovixNavHost(
    navController: NavHostController,
    startDestination: Route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        onBoardingNavGraph(navController)
        authenticationNavGraph(navController)
        homeNavGraph(navController)
        searchNavGraph(navController)
        categoriesNavGraph(navController)
        myListsNavGraph(navController)
        myAccountNavGraph(navController)
        actorDetailsNavGraph(navController)
        movieDetailsNavGraph(navController)
        tvShowDetailsNavGraph(navController)
        reviewsNavGraph(navController)
        continueWatchingNavGraph(navController)
    }
}