package com.baghdad.design_system.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp


fun Modifier.threeSidedBorder(
    width: Dp,
    color: Color,
    cornerRadius: Dp
) = this.drawBehind {
    val cornerRadiusInPx = cornerRadius.toPx()
    val path = Path().apply {
        drawBorderPath(width.toPx(), size, cornerRadiusInPx)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = width.toPx())
    )
}

private fun Path.drawBorderPath(strokeWidth: Float, size: Size, cornerRadius: Float) {
    moveTo(0f, 0f)
    lineTo(size.width - (strokeWidth / 2) - cornerRadius, 0f)

    addTopRightCorner(size.width - (strokeWidth / 2), cornerRadius)

    lineTo(size.width - (strokeWidth / 2), size.height - (strokeWidth / 2) - cornerRadius)

    addBottomRightCorner(size.width - (strokeWidth / 2), size.height - (strokeWidth / 2), cornerRadius)

    lineTo(0f, size.height - (strokeWidth / 2))
}

private fun Path.addTopRightCorner(width: Float, radius: Float) {
    arcTo(
        rect = Rect(
            left = width - radius * 2,
            top = 0f,
            right = width,
            bottom = radius * 2
        ),
        startAngleDegrees = 270f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )
}

private fun Path.addBottomRightCorner(width: Float, height: Float, radius: Float) {
    arcTo(
        rect = Rect(
            left = width - radius * 2,
            top = height - radius * 2,
            right = width,
            bottom = height
        ),
        startAngleDegrees = 0f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )
}