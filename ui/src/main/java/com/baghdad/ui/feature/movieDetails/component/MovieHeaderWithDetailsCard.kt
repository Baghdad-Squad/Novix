package com.baghdad.ui.feature.movieDetails.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.CarousalDot
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.AutoSlidingImageCarousel
import com.baghdad.ui.util.arabicDuration
import com.baghdad.ui.util.isArabicSystemLocale
import com.baghdad.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.baghdad.viewmodel.movieDetails.MovieDetailsState
import com.baghdad.viewmodel.movieDetails.formatDuration

@Composable
fun MovieHeaderWithDetailsCard(
    uiState: MovieDetailsState,
    listener: MovieDetailsInteractionListener,
    modifier: Modifier = Modifier
) {
    val images = if (uiState.movieImages.isEmpty()) listOf(uiState.posterImageURL) else { uiState.movieImages }
    val pagerState = rememberPagerState(pageCount = { images.size })
    val aspectRatio = if (uiState.movieImages.isNotEmpty()) { 1.778f } else { 1.2f }


    Box(modifier = modifier) {
        AutoSlidingImageCarousel(
            imageUrls = images,
            imageAspectRatio = aspectRatio,
            pagerState = pagerState
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 116.dp)
        ) {
            if (uiState.movieImages.size > 1) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Theme.color.iconBackgroundLow)
                        .border(1.dp, Theme.color.stroke, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CarousalDot(
                        totalDots = uiState.movieImages.size,
                        selectedIndex = pagerState.currentPage,
                        modifier = Modifier
                    )
                }
            }

            MovieDetailsHeader(
                title = uiState.movieName,
                releaseDate = uiState.date,
                rating = uiState.rating,
                duration = if (isArabicSystemLocale()) arabicDuration(uiState.duration) else uiState.duration.formatDuration(),
                categories = uiState.categories,
                onViewReviewClicked = {
                    listener.onReviewClick(uiState.movieId)
                },
                onCategoryClick = { listener.onCategoryClick(it) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .then(Modifier.padding(bottom = if (uiState.categories.isEmpty()) 24.dp else 0.dp))
            )
        }
    }
}
