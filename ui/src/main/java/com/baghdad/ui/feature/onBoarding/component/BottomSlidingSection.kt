package com.baghdad.ui.feature.onBoarding.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.CarousalDot
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme

@Composable
fun BottomSlidingSection(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CarousalDot(
            pagerState.pageCount,
            selectedIndex = pagerState.currentPage,
        )

        IconsTransitionButtons(
            pagerState = pagerState,
            onClickNext = onClickNext,
            onClickBack = onClickBack
        )

    }
}



@Composable
private fun IconsTransitionButtons(
    pagerState: PagerState,
    onClickNext: () -> Unit,
    onClickBack: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage > 0,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 },
            exit = fadeOut(tween(300)) + slideOutVertically(tween(300)) { it / 2 }
        ) {
            IconButton(
                icon = painterResource(R.drawable.ic_arrow_right),
                modifier = Modifier.rotate(180f),
                background = Color.Transparent,
                tintIcon = Theme.color.primary,
                borderStroke = BorderStroke(1.dp, Theme.color.stroke),
                shape = RoundedCornerShape(12.dp),
                size = Pair(52.dp, 48.dp),
                onClick = {
                    onClickBack()
                }
            )
        }
        IconButton(
            icon = painterResource(R.drawable.ic_arrow_right),
            background = Theme.color.primary,
            tintIcon = Theme.color.onPrimary,
            shape = RoundedCornerShape(12.dp),
            size = Pair(52.dp, 48.dp),
            onClick = {
                onClickNext()
            }
        )

    }
}
