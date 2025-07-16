package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    snackbar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    bottomBarHeight: Int = 70,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.matchParentSize()
        ) {
            topBar?.invoke()
            content()
            bottomBar?.let {
                Spacer(Modifier.height(bottomBarHeight.dp))
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            floatingActionButton?.let { fab ->
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp, bottom = 10.dp)
                        .align(Alignment.End),
                ) {
                    fab()
                }
            }
            bottomBar?.invoke()
        }
        snackbar?.let { snackbarContent ->
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                snackbarContent()
            }
        }
    }
}