package com.baghdad.ui.navigation.graph.onBoarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.welcome.WelcomeScreen
import com.baghdad.ui.feature.onBoarding.OnBoardingScreen
import com.baghdad.ui.navigation.graph.DummyScreen
import com.baghdad.ui.navigation.route.AuthenticationRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.OnBoardingRoute


fun NavGraphBuilder.onBoardingNavGraph(navController: NavHostController) {
    navigation<Graph.OnBoardingGraph>(startDestination = OnBoardingRoute.OnBoardingScreen) {
        composable<OnBoardingRoute.OnBoardingScreen> {
            OnBoardingScreen(
                handleNavigation = {event ->
                    handleOnBoardingNavigation(
                        event = event,
                        navController = navController
                    )
                }
            )
        }
    }
}

private fun handleOnBoardingNavigation(event: OnBoardingNavEvent, navController: NavController) {
    when (event) {
        OnBoardingNavEvent.NavigateToHome -> navController.navigate(Graph.HomeGraph) {
            popUpTo(Graph.OnBoardingGraph) {
                inclusive = true
            }
        }

        OnBoardingNavEvent.NavigateToLogin -> navController.navigate(Graph.AuthenticationGraph) {
            popUpTo(Graph.OnBoardingGraph) {
                inclusive = true
            }
        }

        OnBoardingNavEvent.NavigateToWelcome -> navController.navigate(OnBoardingRoute.WelcomeScreen){
            popUpTo(Graph.OnBoardingGraph) {
                inclusive = true
            }
        }
    }
}