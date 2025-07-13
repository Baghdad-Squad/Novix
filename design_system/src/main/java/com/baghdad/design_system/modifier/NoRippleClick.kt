package com.baghdad.design_system.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.clickable(
        onClick = onClick,
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled
    )
}

fun Modifier.customBorder(
    borderWidth: Dp,
    color: Color,
    sides: List<Side> = listOf(Side.Top, Side.Bottom, Side.Right) // Exclude right by default
) = composed {
    val borderWidthPx = with(LocalDensity.current) { borderWidth.toPx() }
    drawBehind {
        sides.forEach { side ->
            when (side) {
                Side.Top -> drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = borderWidthPx
                )
                Side.Bottom -> drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderWidthPx
                )
                Side.Left -> {
                    drawLine(
                        color = color,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = borderWidthPx)
                }


                // Skip Side.Right explicitly
                Side.Right -> {
                    drawLine(
                        color = color,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = borderWidthPx)
                }
            }
        }
    }
}

enum class Side { Top, Bottom, Left, Right }