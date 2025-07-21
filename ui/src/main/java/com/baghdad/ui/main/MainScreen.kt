package com.baghdad.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BottomNavItem
import com.baghdad.design_system.component.NovixBottomNavigationBar
import com.baghdad.design_system.component.Scaffold
import com.baghdad.ui.navigation.NovixNavHost
import com.baghdad.ui.navigation.route.Graph

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val bottomNavItems = remember {
        mapOf(
            Graph.HomeGraph to BottomNavItem(
                R.drawable.ic_home_filled,
                R.drawable.ic_home_outlined
            ),
            Graph.SearchGraph to BottomNavItem(
                R.drawable.ic_search_filled,
                R.drawable.ic_search_outlined
            ),
            Graph.CategoriesGraph to BottomNavItem(
                R.drawable.ic_masks_filled,
                R.drawable.ic_masks_outlined
            ),
            Graph.MyListsGraph to BottomNavItem(
                R.drawable.ic_allbookmark_filled,
                R.drawable.ic_allbookmark_outlined
            ),
            Graph.MyAccountGraph to BottomNavItem(
                R.drawable.ic_user_octagon_filled,
                R.drawable.ic_user_octagon_outlined
            ),
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val isMainGraphRoute by remember(navBackStackEntry) {
        derivedStateOf {
            val currentHierarchy = navBackStackEntry?.destination?.hierarchy
            bottomNavItems.keys.any { graph ->
                currentHierarchy?.any { destination ->
                    destination.route == graph::class.qualifiedName
                } ?: false
            }
        }
    }

    val selectedIndex by remember(navBackStackEntry) {
        derivedStateOf {
            val currentHierarchy = navBackStackEntry?.destination?.hierarchy
            bottomNavItems.keys.indexOfFirst { graph ->
                currentHierarchy?.any { destination ->
                    destination.route == graph::class.qualifiedName
                } ?: false
            }.takeIf { it >= 0 } ?: 0
        }
    }

    Scaffold(
        modifier = modifier.navigationBarsPadding(),
        bottomBar = {
            AnimatedVisibility(
                visible = isMainGraphRoute,
                enter = slideInVertically(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(200, 100)),
                exit = slideOutVertically(
                    animationSpec = tween(200, easing = FastOutLinearInEasing),
                    targetOffsetY = { it }
                ) + fadeOut(animationSpec = tween(150))
            ) {
                NovixBottomNavigationBar(
                    items = bottomNavItems.values.toList(),
                    onClick = { index ->
                        val targetGraph = bottomNavItems.keys.elementAt(index)
                        navController.navigate(targetGraph) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    selectedIconIndex = selectedIndex
                )
            }
        }
    ) {
        NovixNavHost(

            navController = navController,
            startDestination = Graph.HomeGraph,
        )
    }
}