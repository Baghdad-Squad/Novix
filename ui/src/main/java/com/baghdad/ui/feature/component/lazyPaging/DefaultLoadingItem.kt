package com.baghdad.ui.feature.component.lazyPaging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.WavyLoadingIndicator

@Composable
fun DefaultLoadingItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        WavyLoadingIndicator()
    }
}