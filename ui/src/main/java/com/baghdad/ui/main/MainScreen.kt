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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baghdad.design_system.component.NovixBottomNavigationBar
import com.baghdad.design_system.component.scaffold.Scaffold
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.model.ContentRestrictionTypes
import com.baghdad.ui.navigation.NovixNavHost
import com.baghdad.ui.navigation.bottom.BOTTOM_NAV_ITEMS
import com.baghdad.ui.navigation.bottom.TOP_LEVEL_ROUTES
import com.baghdad.ui.navigation.bottom.navigateToBottomNavDestination
import com.baghdad.ui.navigation.bottom.rememberBottomNavSelectedIndex
import com.baghdad.ui.navigation.bottom.rememberIsTopLevelMainRoute
import com.baghdad.ui.navigation.route.AuthenticationRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.MovieDetailsRoute
import com.baghdad.ui.navigation.route.TvShowDetailsRoute
import com.baghdad.viewmodel.main.MainState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onStatusBarTransparencyChanged: (transparent: Boolean) -> Unit,
    state: MainState
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isMainGraphRoute by rememberIsTopLevelMainRoute(
        navBackStackEntry,
        BOTTOM_NAV_ITEMS.keys,
        TOP_LEVEL_ROUTES,
    )
    val selectedIndex by rememberBottomNavSelectedIndex(navBackStackEntry, BOTTOM_NAV_ITEMS.keys)

    val animatedBottomPadding by animateDpAsState(
        targetValue = if (isMainGraphRoute) BOTTOM_BAR_HEIGHT.dp else 0.dp,
        animationSpec = BOTTOM_BAR_ANIMATION_SPEC
    )

    LaunchedEffect(currentRoute) {
        val transparentRoutes = setOf(
            MovieDetailsRoute.MovieDetailsScreen::class.qualifiedName,
            TvShowDetailsRoute.TvShowDetailsScreen::class.qualifiedName,
            TvShowDetailsRoute.EpisodeDetailsScreen::class.qualifiedName,
            AuthenticationRoute.WelcomeScreen::class.qualifiedName
        )

        val isTransparent = currentRoute in transparentRoutes
        onStatusBarTransparencyChanged(isTransparent)
    }

    state.isLoggedIn?.let { isLoggedIn ->
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
                    if (!isKeyboardOpen())
                        NovixBottomNavigationBar(
                            items = BOTTOM_NAV_ITEMS.values.toList(), onClick = { index ->
                                if (index != selectedIndex) {
                                    val targetGraph = BOTTOM_NAV_ITEMS.keys.elementAt(index)
                                    navController.navigateToBottomNavDestination(targetGraph)
                                }
                            }, selectedIconIndex = selectedIndex
                        )
                }
            }) {
            CompositionLocalProvider(
                (LocalContentRestriction provides ContentRestrictionTypes.valueOf(state.contentRestriction.name))
            ) {
                NovixNavHost(
                    modifier = Modifier.padding(bottom = animatedBottomPadding),
                    navController = navController,
                    startDestination = if (state.isFirstTimeUser == true) Graph.OnBoardingGraph else if (isLoggedIn) Graph.HomeGraph else Graph.AuthenticationGraph
                )
            }
        }
    }
}

val LocalContentRestriction = staticCompositionLocalOf { ContentRestrictionTypes.STRICT }

private const val BOTTOM_BAR_HEIGHT = 70

private val BOTTOM_BAR_ANIMATION_SPEC = tween<Dp>(300, easing = FastOutSlowInEasing)

private val bottomSheetEnterAnimation = slideInVertically(
    animationSpec = tween(300, easing = FastOutSlowInEasing), initialOffsetY = { it }) + fadeIn(
    animationSpec = tween(200, 100)
)

private val bottomSheetExitAnimation = slideOutVertically(
    animationSpec = tween(200, easing = FastOutLinearInEasing), targetOffsetY = { it }) + fadeOut(
    animationSpec = tween(150)
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun isKeyboardOpen(): Boolean {
    return WindowInsets.isImeVisible
}