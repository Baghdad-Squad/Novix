package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun HotScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: (@Composable () -> Unit)? = null,
    snackbar: (@Composable () -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {  },
    content: @Composable () -> Unit,

) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(Theme.color.surface),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.matchParentSize()
        ) {
            topBar()
            content()
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            bottomBar()
        }
        floatingActionButton?.let { fab ->
            Box(
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 10.dp)
                    .align(Alignment.BottomEnd),
            ) {
                fab()
            }
        }
        snackbar?.let {  snackbarContent ->
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
