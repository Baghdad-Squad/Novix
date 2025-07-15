package com.baghdad.ui.navigation.graph.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.baghdad.ui.navigation.route.Graph

@Composable
inline fun <reified T : Graph> NavBackStackEntry.toGraph(navController: NavController): T {
    val parentEntry = remember(this) {
        navController.getBackStackEntry<T>()
    }
    return parentEntry.toRoute<T>()
}