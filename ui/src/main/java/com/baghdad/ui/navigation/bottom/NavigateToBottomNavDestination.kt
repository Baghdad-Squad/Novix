package com.baghdad.ui.navigation.bottom

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.baghdad.ui.navigation.route.Graph

fun NavController.navigateToBottomNavDestination(newGraph: Graph) {
    navigate(newGraph) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}