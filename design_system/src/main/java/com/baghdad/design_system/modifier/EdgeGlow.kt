package com.baghdad.design_system.modifier

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun Modifier.customEdgeGlow(
    color: Color = Theme.color.primary,
    width: Dp = 533.5.dp,
    height: Dp = 416.4.dp,
    angle: Float = 179.24f,
    opacity: Float = 0.08f,
    blurRadius: Dp = 150.dp,
    offsetX: Dp = (-210).dp,
    offsetY: Dp = (-185).dp
) = composed {
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = opacity * 0.7f, targetValue = opacity, animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
        )
    )

    this.then(
        Modifier.drawWithContent {
            drawContent()

            val paint = Paint().apply {
                this.asFrameworkPaint().maskFilter = BlurMaskFilter(
                    blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL
                )
            }

            rotate(angle) {
                drawRect(
                    brush = Brush.linearGradient(
                        0f to color.copy(alpha = 0f), 1f to color.copy(alpha = glowAlpha)
                    ),
                    topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
                    size = Size(width.toPx(), height.toPx()),
                    blendMode = BlendMode.Screen
                )
            }
        }
    )
}