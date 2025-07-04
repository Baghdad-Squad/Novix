package com.baghdad.design_system.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme


@Composable
fun CarousalDot(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    highlightedSize: Dp,
    defaultSize: Dp
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(totalDots) { index ->
            Dot(
                isHighLighted = index == selectedIndex,
                highlightedSize = highlightedSize,
                defaultSize = defaultSize
            )
        }
    }
}

@Composable
private fun Dot(
    isHighLighted: Boolean,
    highlightedSize: Dp = 7.dp,
    defaultSize: Dp = 5.dp,
    modifier: Modifier = Modifier
) {
    val dotTintColor by animateColorAsState(
        targetValue = if (isHighLighted) Theme.color.primary else Theme.color.hint,
        animationSpec = tween(400)
    )

    val dotSize by animateDpAsState(
        targetValue = if (isHighLighted) highlightedSize else defaultSize,
        animationSpec = tween(400)
    )

    Box(
        modifier = modifier
            .background(color = dotTintColor, shape = CircleShape)
            .size(dotSize)
            .then(
                if (!isHighLighted)
                    Modifier.border(
                        width = 0.5.dp,
                        color = Theme.color.stroke,
                        shape = CircleShape
                    ) else Modifier
            )
    )
}


@PreviewScreenSizes
@Composable
fun PreviewCarousalDot() {
    NovixTheme {
        CarousalDot(
            totalDots = 7,
            selectedIndex = 3,
            modifier = Modifier.fillMaxWidth(),
            highlightedSize = 10.dp,
            defaultSize = 6.dp
        )
    }
}