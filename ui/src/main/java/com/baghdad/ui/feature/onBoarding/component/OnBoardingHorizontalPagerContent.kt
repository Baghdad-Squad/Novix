package com.baghdad.ui.feature.onBoarding.component

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    imageWidthFraction: Float = if (isTablet()) 0.50f else 0.80f,
) {
    var previousPage by remember { mutableIntStateOf(pagerState.currentPage) }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage > previousPage) {
            onNext()
        } else if (pagerState.currentPage < previousPage) {
            onBack()
        }
        previousPage = pagerState.currentPage
    }

    Box(modifier = Modifier.offset(y = (-100).dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(imageWidthFraction)
                .height(150.dp)
                .dropShadow(
                    CircleShape,
                    color = Theme.color.primary.copy(0.2f),
                    alpha = 0.2f,
                    blur = 50.dp,
                    offsetY = (-24).dp,
                    spread = 1.dp,
                )
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val pageOffset =
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            Column(
                modifier = modifier
                    .graphicsLayer {
                        translationX = pageOffset * size.width * 0.2f
                        alpha = 1f - (0.9f * abs(pageOffset))
                    }
                    .animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageAnimated(
                    page = page,
                    onBoardingInfo = onBoardingInfo,
                    pageOffset = pageOffset,
                    imageWidthFraction = imageWidthFraction
                )

                TextSlidingAnimationVisibility(
                    onBoardingInfo = onBoardingInfo,
                    currentPage = page,
                    pageOffset = pageOffset
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
    imageWidthFraction: Float = if (isTablet()) 0.50f else 0.80f,
) {
    Crossfade(
        targetState = page,
        animationSpec = tween(durationMillis = 500)
    ) { currentPage ->
        Image(
            painter = painterResource(onBoardingInfo[currentPage].imageIndex),
            contentDescription = stringResource(onBoardingInfo[currentPage].title),
            modifier = Modifier
                .fillMaxWidth(imageWidthFraction)
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
private fun TextSlidingAnimationVisibility(
    onBoardingInfo: List<OnBoardingInfo>,
    currentPage: Int,
    pageOffset: Float = 0f,
) {
        Column(
            modifier = Modifier.graphicsLayer {
                val scale = 1f - (0.5f * abs(pageOffset))
                scaleX = scale
                scaleY = scale
            },
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(onBoardingInfo[currentPage].title),
                style = Theme.typography.title.large,
                color = Theme.color.title,
                modifier = Modifier.padding(top = 32.dp),
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = stringResource(onBoardingInfo[currentPage].description),
                style = Theme.typography.body.medium,
                color = Theme.color.body,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                lineHeight = TextUnit(24f, TextUnitType.Sp),
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}

