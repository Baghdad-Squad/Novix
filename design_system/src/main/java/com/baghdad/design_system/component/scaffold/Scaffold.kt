package com.baghdad.design_system.component.scaffold

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.SnackBarPosition
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

private val DefaultFabPadding = 12.dp
private val DefaultFabBottomPadding = 10.dp
private val DefaultSnackBarPadding = 16.dp

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    topBar: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    backgroundContent: (@Composable () -> Unit)? = null,
    snackBarState: ScaffoldSnackBarState = ScaffoldSnackBarState(),
    onSnackBarActionClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        backgroundContent?.invoke()

        Column(
            modifier = Modifier.matchParentSize(),
        ) {

            topBar?.invoke()

            Box {
                Crossfade(targetState = isLoading) { isLoading ->
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
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .imePadding(),
            horizontalAlignment = Alignment.End,
        ) {

            PositionedSnackBar(
                modifier = Modifier.padding(DefaultSnackBarPadding),
                state = snackBarState,
                onActionClick = onSnackBarActionClick,
                position = SnackBarPosition.BOTTOM,
            )

            floatingActionButton?.let { fab ->
                Box(
                    modifier =
                        Modifier
                            .padding(
                                end = DefaultFabPadding,
                                bottom = DefaultFabBottomPadding,
                            ).align(Alignment.End),
                ) {
                    fab()
                }
            }

            bottomBar?.invoke()
        }

        PositionedSnackBar(
            modifier =
                Modifier
                    .statusBarsPadding()
                    .padding(DefaultSnackBarPadding)
                    .align(Alignment.TopCenter),
            state = snackBarState,
            onActionClick = onSnackBarActionClick,
            position = SnackBarPosition.TOP,
        )
    }
}

@Composable
private fun PositionedSnackBar(
    state: ScaffoldSnackBarState,
    position: SnackBarPosition,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    SnackBar(
        modifier = modifier,
        message = state.message,
        isSuccess = state.isSuccess,
        isVisible = state.isVisible && state.position == position,
        actionLabel = state.actionLabel,
        onActionClick = onActionClick,
    )
}

@NovixPreviews
@Composable
private fun ScaffoldPreview() {
    NovixTheme {
        Scaffold(
            modifier = Modifier.background(Theme.color.surface),
            backgroundContent = { BackgroundBlur() },
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(text = "Content", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
