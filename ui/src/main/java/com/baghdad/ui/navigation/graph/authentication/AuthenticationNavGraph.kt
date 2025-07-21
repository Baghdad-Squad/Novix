package com.baghdad.ui.navigation.graph.authentication

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.login.LoginScreen
import com.baghdad.ui.navigation.route.AuthenticationRoute
import com.baghdad.ui.navigation.route.Graph

fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {
    navigation<Graph.AuthenticationGraph>(
        startDestination = AuthenticationRoute.LoginScreen,
    ) {
        composable<AuthenticationRoute.LoginScreen> {
            LoginScreen()
        }
    }
}

private fun handleAuthenticationNavigation(
    event: AuthenticationNavEvent,
    navController: NavHostController
) {
    when (event) {
        AuthenticationNavEvent.NavigateToHome -> navController.navigate(Graph.HomeGraph) {
            popUpTo(Graph.AuthenticationGraph) {
                inclusive = true
            }
        }
    }
}