package com.baghdad.design_system.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    isLoading: Boolean? = null,
    topBar: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    snackbar: (@Composable () -> Unit)? = null,
    isSnackBarWithActionLabel: Boolean = false,
    snackBarPosition: SnackBarPosition = if (isSnackBarWithActionLabel) SnackBarPosition.BOTTOM else SnackBarPosition.TOP,
    bottomBar: (@Composable () -> Unit)? = null,
    bottomBarHeight: Int = 70,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.matchParentSize(),
        ) {
            topBar?.invoke()
            Box {
                Crossfade(targetState = isLoading == true, modifier = modifier) { isLoading ->
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            WavyLoadingIndicator()
                        }
                    } else {
                        content()
                    }
                }
            }
            bottomBar?.let {
                Spacer(Modifier.height(bottomBarHeight.dp))
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .imePadding(),
                horizontalAlignment = Alignment.End,
        ) {
            if (snackBarPosition == SnackBarPosition.BOTTOM) {
                snackbar?.let { snackbarContent ->
                    Box(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        snackbarContent()
                    }
                }
            }
            floatingActionButton?.let { fab ->
                Box(
                    modifier =
                        Modifier
                            .padding(end = 12.dp, bottom = 10.dp)
                            .align(Alignment.End),
                        ) {
                    fab()
                }
            }
            bottomBar?.invoke()
        }
        if (snackBarPosition == SnackBarPosition.TOP) {
            snackbar?.let { snackBarContent ->
                Box(
                    modifier =
                        Modifier
                            .statusBarsPadding()
                            .padding(16.dp)
                            .align(Alignment.TopCenter),
                ) {
                    snackBarContent()
                }
            }
        }
    }
}

enum class SnackBarPosition {
    TOP,
    BOTTOM,
}
