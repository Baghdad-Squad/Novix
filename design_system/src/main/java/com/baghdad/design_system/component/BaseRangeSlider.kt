package com.baghdad.design_system.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 8.dp
) {
    val primaryColor = Theme.color.primary
    val inactiveTrackColor = Theme.color.surfaceHigh
    val borderColor = Theme.color.stroke
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        RangeSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = modifier
                .fillMaxWidth()
                .height(trackHeight)
                .padding(horizontal = 12.dp),
            colors = SliderDefaults.colors(
                thumbColor = primaryColor,
                activeTrackColor = primaryColor,
                inactiveTrackColor = inactiveTrackColor
            ),
            track = {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .border(1.dp, borderColor, RoundedCornerShape(100.dp))
                        .clip(RoundedCornerShape(100.dp))
                ) {
                    val trackWidth = size.width
                    val trackPxHeight = trackHeight.toPx()
                    val centerY = size.height / 2

                    // Inactive track
                    drawRect(
                        color = inactiveTrackColor,
                        topLeft = Offset(0f, centerY - trackPxHeight / 2),
                        size = Size(trackWidth, trackPxHeight)
                    )

                    // Active track
                    val startPos =
                        ((value.start - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * trackWidth
                    val endPos =
                        ((value.endInclusive - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * trackWidth

                    if (endPos > startPos) {
                        drawRect(
                            color = primaryColor,
                            topLeft = Offset(startPos, centerY - trackPxHeight / 2),
                            size = Size(endPos - startPos, trackPxHeight)
                        )
                    }
                }
            },
            startThumb = {
                SliderThumb()
            },
            endThumb = {
                SliderThumb()
            },
        )
    }
}

@Composable
private fun SliderThumb(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(16.dp)
            .background(Theme.color.primary)
    )
}

