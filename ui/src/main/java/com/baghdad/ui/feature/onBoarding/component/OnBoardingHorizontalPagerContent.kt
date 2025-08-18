package com.baghdad.ui.feature.onBoarding.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.dropShadow
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.onBoarding.OnBoardingInfo
import kotlin.math.abs

@Composable
fun OnBoardingHorizontalPagerContent(
    pagerState: PagerState,
    onBoardingInfo: List<OnBoardingInfo>,
    modifier: Modifier = Modifier,
) {

    Box(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.Center)
                .height(250.dp)
                .dropShadow(
                    CircleShape,
                    color = Theme.color.primary.copy(0.2f),
                    alpha = 0.3f,
                    blur = 50.dp,
                    offsetY = (-24).dp,
                    spread = 1.dp,
                )
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { page ->
            val pageOffset =
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ImageAnimated(
                    page = page,
                    onBoardingInfo = onBoardingInfo,
                    pageOffset = pageOffset,
                )
            }
        }
    }
}


@Composable
private fun ImageAnimated(
    page: Int,
    onBoardingInfo: List<OnBoardingInfo>,
    pageOffset: Float,
) {
    Crossfade(
        targetState = page,
    ) { currentPage ->
        Image(
            painter = painterResource(onBoardingInfo[currentPage].imageIndex),
            contentDescription = stringResource(onBoardingInfo[currentPage].title),
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .graphicsLayer {
                    val scale = 1f - (0.9f * abs(pageOffset))
                    scaleX = scale
                    scaleY = scale
                }
                .height(250.dp),
        )
    }

}

@Composable
fun TextSlidingAnimationVisibility(
    onBoardingInfo: List<OnBoardingInfo>,
    currentPage: Int,
    pageOffset: Float = 0f
) {
    Column(
        modifier = Modifier.graphicsLayer {
            val scale = 1f - (0.5f * abs(pageOffset))
            scaleX = scale
            scaleY = scale
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(onBoardingInfo[currentPage].title),
            style = Theme.typography.title.large,
            color = Theme.color.title,
            textAlign = TextAlign.Center
            )

        Text(
            text = stringResource(onBoardingInfo[currentPage].description),
            style = Theme.typography.body.medium,
            color = Theme.color.body,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center,
        )
    }
}
