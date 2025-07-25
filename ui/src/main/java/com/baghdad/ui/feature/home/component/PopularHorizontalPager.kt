package com.baghdad.ui.feature.home.component

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.CarousalDot
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import kotlinx.coroutines.delay

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopularCardPager(
    items: List<PopularItemUiState>,
    isLoading: Boolean,
    onClick: (PopularItemUiState) -> Unit,
    onSaveClick: (PopularItemUiState) -> Unit,
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 4000L,
) {
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState { items.size }
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = 188.dp

    val horizontalPadding =
        maxOf(
            16.dp,
            (screenWidth - cardWidth) / 2 - 2.dp,
        )
    if (items.isNotEmpty()) {
        LaunchedEffect(items) {
            while (true) {
                delay(autoSlideDuration)
                val next = (pagerState.currentPage + 1) % items.size
                pagerState.animateScrollToPage(next)
            }
        }
    }

    Crossfade(isLoading) { isLoading ->
        if (isLoading) {
            LoadingPopularCardPager(
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                modifier = modifier,
            )
        } else {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = horizontalPadding),
                        beyondViewportPageCount = 1,
                        modifier = Modifier.fillMaxWidth(),
                    ) { page ->
                        val currentPageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        val rotation =
                            when {
                                currentPageOffset < -0.5f -> 3f
                                currentPageOffset > 0.5f -> -3f
                                else -> 0f
                            }

                        val xScale =
                            when {
                                currentPageOffset == 0f -> 1f
                                else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.1f)
                            }

                        val yScale =
                            when {
                                currentPageOffset == 0f -> 1f
                                else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.15f)
                            }

                        val yTranslation =
                            when {
                                currentPageOffset == 0f -> 0f
                                else -> kotlin.math.abs(currentPageOffset) * 40f
                            }

                        val item = items[page]

                        PopularCard(
                            contentName = item.name,
                            contentRating = item.rating,
                            imageUrl = item.imageUrl,
                            onCardClick = { onClick(item) },
                            onSavedClick = { onSaveClick(item) },
                            isSaved = item.isSaved,
                            modifier =
                                Modifier.graphicsLayer {
                                    rotationZ = rotation
                                    scaleX = xScale
                                    scaleY = yScale
                                    translationY = yTranslation
                                },
                        )
                    }
                    CarousalDot(
                        totalDots = items.size,
                        selectedIndex = pagerState.currentPage,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingPopularCardPager(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = 1) { 3 }
    HorizontalPager(
        state = pagerState,
        contentPadding = contentPadding,
        beyondViewportPageCount = 1,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            ) { page ->
        val currentPageOffset =
            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

        val rotation =
            when {
                currentPageOffset < -0.5f -> 3f
                currentPageOffset > 0.5f -> -3f
                else -> 0f
            }

        val xScale =
            when {
                currentPageOffset == 0f -> 1f
                else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.1f)
            }

        val yScale =
            when {
                currentPageOffset == 0f -> 1f
                else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.15f)
            }

        val yTranslation =
            when {
                currentPageOffset == 0f -> 0f
                else -> kotlin.math.abs(currentPageOffset) * 40f
            }

        LoadingPopularCard(
            isCentralCard = currentPageOffset == 0f,
            modifier =
                Modifier
                    .graphicsLayer {
                        rotationZ = rotation
                        scaleX = xScale
                        scaleY = yScale
                        translationY = yTranslation
                    }
                    .fillMaxWidth(),
        )
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoadingPopularCardPagerPreview() {
    NovixTheme(isDarkTheme = true) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Theme.color.surface),
        ) {
            LoadingPopularCardPager(PaddingValues(0.dp))
        }
    }
}
