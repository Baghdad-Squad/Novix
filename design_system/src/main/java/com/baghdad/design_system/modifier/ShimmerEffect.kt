package com.baghdad.design_system.modifier

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "Shimmer Transition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = "start offset"
    )

    val shimmerColors = remember {
        listOf(
            Color(0x3DFFFFFF),
            Color(0x00FFFFFF),
            Color(0x14FFFFFF),
        )
    }
    val shimmerBrush = remember(size, startOffsetX) {
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    }
    onGloballyPositioned { coordinates ->
        size = coordinates.size
    }
        .drawWithCache {

            onDrawWithContent {
                drawContent()
                drawRect(brush = shimmerBrush)
            }
        }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED)
@Composable
private fun ShimmerPreview() {
    NovixTheme(isDarkTheme = true) {
        Box(
            modifier = Modifier
                .size(height = 150.dp, width = 300.dp)
                .background(Theme.color.surface)
                .shimmerEffect()
        )
    }
}