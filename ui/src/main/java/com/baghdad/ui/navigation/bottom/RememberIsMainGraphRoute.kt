package com.baghdad.ui.navigation.bottom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import com.baghdad.ui.navigation.route.Graph

@Composable
fun rememberIsMainGraphRoute(
    navBackStackEntry: NavBackStackEntry?,
    graphRoutes: Set<Graph>
): State<Boolean> {
    return remember(navBackStackEntry) {
        derivedStateOf {
            val hierarchy = navBackStackEntry?.destination?.hierarchy
            graphRoutes.any { graph ->
                hierarchy?.any { destination ->
                    destination.route == graph::class.qualifiedName
                } ?: false
            }
        }
    }
}