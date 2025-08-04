package com.baghdad.ui.feature.onBoarding.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
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
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.onBoarding.OnBoardingInfo
import com.baghdad.viewmodel.onBoarding.OnBoardingState
import kotlin.math.abs

@Composable
fun OnBoardingHorizontalPagerContent(
    pagerState: PagerState,
    state: OnBoardingState,
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
            modifier = modifier
                .offset(y = 64.dp)
                .height(150.dp)
                .blur(
                    radiusX = 100.dp,
                    radiusY = 100.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(
                    animateColorAsState(
                        targetValue = Theme.color.primary.copy(0.2f),
                        animationSpec = tween(durationMillis = 1000)
                    ).value,
                    shape = RoundedCornerShape(100)
                )
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 32.dp)
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
                    state = state,
                    pageOffset = pageOffset,
                    imageWidthFraction = imageWidthFraction
                )

                TextSlidingAnimationVisibility(
                    onBoardingInfo = state.onBoardingInfo,
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
    state: OnBoardingState,
    pageOffset: Float,
    imageWidthFraction: Float = if (isTablet()) 0.50f else 0.80f,
) {
    Crossfade(
        targetState = page,
        animationSpec = tween(durationMillis = 500)
    ) { currentPage ->
        Image(
            painter = painterResource(
                when (currentPage) {
                    0 -> R.drawable.img_on_boarding_1
                    1 -> R.drawable.img_on_boarding_2
                    else -> R.drawable.img_on_boarding_3
                }
            ),
            contentDescription = stringResource(state.onBoardingInfo[currentPage].title),
            modifier = Modifier
                .fillMaxWidth(imageWidthFraction)
                .graphicsLayer {
                    val scale = 1f - (0.9f * abs(pageOffset))
                    scaleX = scale
                    scaleY = scale
                }
        )
    }

}

@Composable
private fun TextSlidingAnimationVisibility(
    onBoardingInfo: List<OnBoardingInfo>,
    currentPage: Int,
    pageOffset: Float,
) {
    AnimatedVisibility(
        visible = abs(pageOffset) < 0.5f,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}

