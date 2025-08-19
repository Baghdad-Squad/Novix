package com.baghdad.ui.navigation.bottom

import androidx.navigation.NavController
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.HomeRoute

fun NavController.navigateToBottomNavDestination(newGraph: Graph) {
    navigate(newGraph) {
        popUpTo(HomeRoute.HomeScreen) {
            saveState = false
            inclusive = false
        }
        launchSingleTop = true
        restoreState = false
    }
}