package com.baghdad.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baghdad.design_system.component.NovixBottomNavigationBar
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.navigation.NovixNavHost
import com.baghdad.ui.navigation.bottom.BOTTOM_NAV_ITEMS
import com.baghdad.ui.navigation.bottom.navigateToBottomNavDestination
import com.baghdad.ui.navigation.bottom.rememberBottomNavSelectedIndex
import com.baghdad.ui.navigation.bottom.rememberIsMainGraphRoute
import com.baghdad.ui.navigation.route.Graph

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val isMainGraphRoute by rememberIsMainGraphRoute(navBackStackEntry, BOTTOM_NAV_ITEMS.keys)

    val selectedIndex by rememberBottomNavSelectedIndex(navBackStackEntry, BOTTOM_NAV_ITEMS.keys)

    val animatedBottomPadding by animateDpAsState(
        targetValue = if (isMainGraphRoute) BOTTOM_BAR_HEIGHT.dp else 0.dp,
        animationSpec = BOTTOM_BAR_ANIMATION_SPEC
    )

    Scaffold(
        modifier = modifier
            .background(Theme.color.surface)
            .fillMaxSize()
            .navigationBarsPadding(),
        bottomBar = {
            AnimatedVisibility(
                visible = isMainGraphRoute,
                enter = bottomSheetEnterAnimation,
                exit = bottomSheetExitAnimation
            ) {
                NovixBottomNavigationBar(
                    items = BOTTOM_NAV_ITEMS.values.toList(),
                    onClick = { index ->
                        val targetGraph = BOTTOM_NAV_ITEMS.keys.elementAt(index)
                        navController.navigateToBottomNavDestination(targetGraph)
                    },
                    selectedIconIndex = selectedIndex
                )
            }
        }
    ) {
        NovixNavHost(
            modifier = Modifier.padding(bottom = animatedBottomPadding),
            navController = navController,
            startDestination = Graph.HomeGraph,
        )
    }
}


private const val BOTTOM_BAR_HEIGHT = 70

private val BOTTOM_BAR_ANIMATION_SPEC = tween<Dp>(300, easing = FastOutSlowInEasing)

private val bottomSheetEnterAnimation = slideInVertically(
    animationSpec = tween(300, easing = FastOutSlowInEasing),
    initialOffsetY = { it }
) + fadeIn(animationSpec = tween(200, 100))

private val bottomSheetExitAnimation = slideOutVertically(
    animationSpec = tween(200, easing = FastOutLinearInEasing),
    targetOffsetY = { it }
) + fadeOut(animationSpec = tween(150))

