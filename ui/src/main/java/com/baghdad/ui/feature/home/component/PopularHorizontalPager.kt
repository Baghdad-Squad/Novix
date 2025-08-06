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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.baghdad.design_system.component.CarousalDot
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import kotlinx.coroutines.delay
import kotlin.math.abs

private const val ROTATION_OFFSET_ADJUSTMENT = 1f
private const val ROTATION_FRACTION_MULTIPLIER = 0.5f
private const val SCALE_CURRENT = 1f
private const val SCALE_SIDE_CARDS = 0.8f

private const val TRANSFORM_ORIGIN_X = 0.5f
private const val TRANSFORM_ORIGIN_Y = 0.9f
private const val PAGE_SPACING_DP = 8
private const val CARD_WIDTH = 188

private const val MIN_FRACTION = 0f
private const val MAX_FRACTION = 1f
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
    LocalConfiguration.current
    val virtualPagedCount = if (items.isEmpty()) 0 else items.size * 1000
    val pagerState =
        rememberPagerState(initialPage = 1) { virtualPagedCount }
    val density = LocalDensity.current

    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
    val horizontalPadding = (screenWidth - CARD_WIDTH.dp) / 2

    if (items.isNotEmpty()) {
        LaunchedEffect(items) {
            pagerState.animateScrollToPage(items.size + 1)
            delay(autoSlideDuration)
            while (true) {
                delay(autoSlideDuration)
                val next = (pagerState.currentPage + 1)
                pagerState.animateScrollToPage(next)
            }
        }
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
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
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = horizontalPadding),
                        beyondViewportPageCount = 1,
                        pageSpacing = PAGE_SPACING_DP.dp,
                        modifier = Modifier.fillMaxWidth(),
                    ) { page ->
                        val currentPageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        val rotation = lerp(
                            start = 3f,
                            stop = -3f,
                            fraction = (currentPageOffset + ROTATION_OFFSET_ADJUSTMENT) * ROTATION_FRACTION_MULTIPLIER
                        )

                        val yScale = lerp(
                            start = SCALE_CURRENT,
                            stop = SCALE_SIDE_CARDS,
                            fraction = abs(currentPageOffset).coerceIn(
                                MIN_FRACTION,
                                MAX_FRACTION
                            )
                        )

                        val item =
                            items[if (items.isEmpty()) return@HorizontalPager else page % items.size]

                        PopularCard(
                            contentName = item.name,
                            contentRating = item.rating,
                            imageUrl = item.imageUrl,
                            onCardClick = { onClick(item) },
                            onSavedClick = { onSaveClick(item) },
                            isSaved = item.isSaved,
                            isCentralCard = currentPageOffset == 0f,
                            isSaveToListVisible = item.type == PopularItemUiState.Type.MOVIE,
                            modifier =
                                Modifier.graphicsLayer {
                                    rotationZ = rotation
                                    scaleY = yScale
                                    transformOrigin = TransformOrigin(
                                        TRANSFORM_ORIGIN_X,
                                        TRANSFORM_ORIGIN_Y
                                    )

                                },
                        )
                    }
                    CarousalDot(
                        totalDots = items.size,
                        selectedIndex = if (items.isEmpty()) return@Crossfade else (pagerState.currentPage % items.size),
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
            remember(pagerState.currentPage, pagerState.currentPageOffsetFraction, page) {
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            }

        val rotation =
            when {
                currentPageOffset < -0.5f -> 3f
                currentPageOffset > 0.5f -> -3f
                else -> 0f
            }

        val xScale =
            when {
                currentPageOffset == 0f -> 1f
                else -> 1f - (abs(currentPageOffset) * 0.1f)
            }

        val yScale =
            when {
                currentPageOffset == 0f -> 1f
                else -> 1f - (abs(currentPageOffset) * 0.15f)
            }

        val yTranslation =
            when {
                currentPageOffset == 0f -> 0f
                else -> abs(currentPageOffset) * 40f
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
