package com.baghdad.ui.navigation.graph.authentication

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.authentication.ForgotPasswordWebViewScreen
import com.baghdad.ui.feature.authentication.LoginScreen
import com.baghdad.ui.feature.authentication.SignUpWebViewScreen
import com.baghdad.ui.navigation.route.AuthenticationRoute
import com.baghdad.ui.navigation.route.AuthenticationRoute.ForgotPasswordScreen
import com.baghdad.ui.navigation.route.AuthenticationRoute.SignUpScreen
import com.baghdad.ui.navigation.route.Graph

fun NavGraphBuilder.authenticationNavGraph(navController: NavHostController) {
    navigation<Graph.AuthenticationGraph>(
        startDestination = AuthenticationRoute.LoginScreen,
    ) {
        composable<AuthenticationRoute.LoginScreen> {
            LoginScreen(
                handleNavigation = { event ->
                    handleAuthenticationNavigation(event, navController)
                }
            )
        }
        composable<SignUpScreen> {
            SignUpWebViewScreen(
                handleNavigation = {
                    handleAuthenticationNavigation(
                        event = it,
                        navController = navController
                    )
                }
            )
        }
        composable<ForgotPasswordScreen> {
            ForgotPasswordWebViewScreen(
                handleNavigation = {
                    handleAuthenticationNavigation(
                        event = it,
                        navController = navController
                    )
                }
            )
        }
    }
}

private fun handleAuthenticationNavigation(
    event: AuthenticationNavEvent,
    navController: NavHostController
) {
    when (event) {
        AuthenticationNavEvent.NavigateToHome -> {
            navController.navigate(Graph.HomeGraph) {
                popUpTo(Graph.AuthenticationGraph) {
                    inclusive = true
                }
            }
        }
        AuthenticationNavEvent.NavigateToForgotPassword -> {
            navController.navigate(ForgotPasswordScreen)
        }

        AuthenticationNavEvent.NavigateToRegister -> {
            navController.navigate(SignUpScreen)
        }

        AuthenticationNavEvent.NavigateBack -> {
            navController.popBackStack()
        }
    }
}
