package com.baghdad.ui.feature.home.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.CarousalDot
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState
import kotlinx.coroutines.delay

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
    val pagerState = rememberPagerState(
        initialPage = items.size / 2,
        pageCount = { items.size }
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
            LoadingPopularCardPage(modifier = modifier)
        } else {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 64.dp),
                    pageSpacing = 4.dp,
                    beyondViewportPageCount = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { page ->
                    val currentPageOffset =
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                    val rotation = when {
                        currentPageOffset < -0.5f -> 3f
                        currentPageOffset > 0.5f -> -3f
                        else -> 0f
                    }

                    val xScale = when {
                        currentPageOffset == 0f -> 1f
                        else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.1f)
                    }

                    val yScale = when {
                        currentPageOffset == 0f -> 1f
                        else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.15f)
                    }

                    val yTranslation = when {
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
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = rotation
                                scaleX = xScale
                                scaleY = yScale
                                translationY = yTranslation
                            }
                            .fillMaxWidth()
                    )
                }
                CarousalDot(
                    totalDots = items.size,
                    selectedIndex = pagerState.currentPage
                )
            }
        }
    }
}


@Composable
private fun LoadingPopularCardPager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(initialPage = 1) { 3 }
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 64.dp),
        pageSpacing = 4.dp,
        beyondViewportPageCount = 1,
        modifier = Modifier
            .fillMaxWidth()
    ) { page ->
        val currentPageOffset =
            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

        val rotation = when {
            currentPageOffset < -0.5f -> 3f
            currentPageOffset > 0.5f -> -3f
            else -> 0f
        }

        val xScale = when {
            currentPageOffset == 0f -> 1f
            else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.1f)
        }

        val yScale = when {
            currentPageOffset == 0f -> 1f
            else -> 1f - (kotlin.math.abs(currentPageOffset) * 0.15f)
        }

        val yTranslation = when {
            currentPageOffset == 0f -> 0f
            else -> kotlin.math.abs(currentPageOffset) * 40f
        }

        PopularCard(
            contentName = item.name,
            contentRating = item.rating,
            imageUrl = item.imageUrl,
            onCardClick = { onClick(item) },
            onSavedClick = { onSaveClick(item) },
            isSaved = item.isSaved,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = rotation
                    scaleX = xScale
                    scaleY = yScale
                    translationY = yTranslation
                }
                .fillMaxWidth()
        )
    }
}