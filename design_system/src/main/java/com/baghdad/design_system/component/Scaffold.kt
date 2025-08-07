package com.baghdad.design_system.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    isLoading: Boolean? = null,
    topBar: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    snackbar: (@Composable (SnackBarPosition) -> Unit)? = null,
    isSnackBarWithActionLabel: Boolean = false,
    snackBarPosition: SnackBarPosition = if (isSnackBarWithActionLabel) SnackBarPosition.BOTTOM else SnackBarPosition.TOP,
    bottomBar: (@Composable () -> Unit)? = null,
    bottomBarHeight: Int = 70,
    backgroundBlur: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        backgroundBlur()
        Column(
            modifier = Modifier.matchParentSize(),
        ) {
            topBar?.invoke()
            Box {
                Crossfade(targetState = isLoading == true) { isLoading ->
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
            snackbar?.let { snackbarContent ->
                Box(
                    modifier = Modifier.padding(16.dp),
                ) {
                    snackbarContent(SnackBarPosition.BOTTOM)
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
        snackbar?.let { snackBarContent ->
            Box(
                modifier =
                    Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    ) {
                snackBarContent(SnackBarPosition.TOP)
            }
        }
    }
}


@NovixPreviews
@Composable
private fun ScaffoldPreview() {
    NovixTheme {
        Scaffold(
            modifier = Modifier.background(Theme.color.surface),
            backgroundBlur = { BackgroundBlur() }) {
            Box(Modifier.fillMaxSize()) {
                Text(text = "Content", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
