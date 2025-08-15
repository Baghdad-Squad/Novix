package com.baghdad.design_system.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WavyLoadingIndicator(modifier: Modifier = Modifier) {
    
    val infiniteTransition = rememberInfiniteTransition(label = "Wavy Progress")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Progress Animation"
    )

    CircularWavyProgressIndicator(
        progress = { animatedProgress },
        color = Theme.color.primary,
        trackColor = Theme.color.stroke,
        modifier = modifier
    )

}

@NovixPreviews
@Composable
private fun WavyLoadingIndicatorPreview() {
    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WavyLoadingIndicator()
        }
    }
}


