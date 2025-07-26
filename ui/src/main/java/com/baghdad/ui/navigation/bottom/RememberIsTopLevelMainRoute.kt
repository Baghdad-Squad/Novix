package com.baghdad.ui.navigation.bottom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.Route

@Composable
fun rememberIsTopLevelMainRoute(
    navBackStackEntry: NavBackStackEntry?,
    mainGraphRoutes: Set<Graph>,
    topLevelRoutes: Set<Route>,
): State<Boolean> =
    remember(navBackStackEntry) {
        derivedStateOf {
            val currentRoute = navBackStackEntry?.destination?.route
            val hierarchy = navBackStackEntry?.destination?.hierarchy

            // Check if current route is a top-level route
            val isTopLevelRoute =
                topLevelRoutes.any { route ->
                    currentRoute?.contains(route::class.qualifiedName.orEmpty()) == true
                }

            // Check if we're within one of the main graphs
            val isInMainGraph =
                mainGraphRoutes.any { graph ->
                    hierarchy?.any { destination ->
                        destination.route?.contains(graph::class.qualifiedName.orEmpty()) == true
                    } ?: false
                }

            isTopLevelRoute && isInMainGraph
        }
    }
