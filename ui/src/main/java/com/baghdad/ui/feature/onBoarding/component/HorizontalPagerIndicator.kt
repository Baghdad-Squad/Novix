package com.baghdad.ui.feature.onBoarding.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Theme.color.primary,
    inactiveColor: Color = Theme.color.body,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { index ->
            val color by animateColorAsState(
                targetValue = if (pagerState.currentPage == index) activeColor else inactiveColor,
                animationSpec = tween(
                    durationMillis = 300,
                ),
                label = "indicatorColorAnimation"
            )
            Box(
                modifier = Modifier
                    .size(if (index == pagerState.currentPage) 8.dp else 5.dp)
                    .background(color, shape = CircleShape)
            )
        }
    }
}


