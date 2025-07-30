package com.baghdad.design_system.modifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


fun Modifier.threeSidedBorder(
    width: Dp,
    color: Color,
    cornerRadius: Dp,
    isRTL: Boolean = false
) = this.drawBehind {
    val cornerRadiusInPx = cornerRadius.toPx()
    val path = Path().apply {
        if (isRTL) {
            drawBorderPathRtl(width.toPx(), size, cornerRadiusInPx)
        } else {
            drawBorderPath(width.toPx(), size, cornerRadiusInPx)

        }
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

    addBottomRightCorner(
        size.width - (strokeWidth / 2),
        size.height - (strokeWidth / 2),
        cornerRadius
    )

    lineTo(0f, size.height - (strokeWidth / 2))
}

private fun Path.drawBorderPathRtl(strokeWidth: Float, size: Size, cornerRadius: Float) {
    val halfStroke = strokeWidth / 2f

    moveTo(size.width, 0f)
    lineTo(halfStroke + cornerRadius, 0f)
    addTopLeftCorner(offsetX = halfStroke, radius = cornerRadius)

    lineTo(halfStroke, size.height - halfStroke - cornerRadius)
    addBottomLeftCorner(
        offsetX = halfStroke,
        height = size.height - halfStroke,
        radius = cornerRadius
    )
    lineTo(size.width, size.height - halfStroke)
}


private fun Path.addTopLeftCorner(offsetX: Float, radius: Float) {
    arcTo(
        rect = Rect(
            left = offsetX,
            top = 0f,
            right = offsetX + radius * 2,
            bottom = radius * 2
        ),
        startAngleDegrees = 270f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )
}


private fun Path.addBottomLeftCorner(offsetX: Float, height: Float, radius: Float) {
    arcTo(
        rect = Rect(
            left = offsetX,
            top = height - radius * 2,
            right = offsetX + radius * 2,
            bottom = height
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, showSystemUi = true)
@Composable
fun PreviewThreeSidedBorder() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .threeSidedBorder(2.dp, Color.Black, 10.dp, true)
    ) {}
}