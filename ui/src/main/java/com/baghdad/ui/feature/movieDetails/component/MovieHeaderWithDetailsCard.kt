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
import com.baghdad.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.baghdad.viewmodel.movieDetails.MovieDetailsState

@Composable
fun MovieHeaderWithDetailsCard(
    state: MovieDetailsState,
    listener: MovieDetailsInteractionListener,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { state.movieImages.size })

    Box(modifier = modifier) {
        AutoSlidingImageCarousel(
            imageUrls = state.movieImages,
            imageAspectRatio = 1.778f,
            pagerState = pagerState
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 116.dp)
        ) {
            if (state.movieImages.size > 1) {
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
                        totalDots = state.movieImages.size,
                        selectedIndex = pagerState.currentPage,
                        modifier = Modifier
                    )
                }
            }

            MovieDetailsHeader(
                title = state.movieName,
                releaseDate = state.date,
                rating = state.rating,
                duration = state.duration,
                categories = state.categories,
                onViewReviewClicked = {
                    listener.onReviewClick(state.movieId)
                },
                onCategoryClick = { listener.onCategoryClick(it) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .then(
                        if (state.categories.isEmpty()) {
                            Modifier.padding(bottom = 24.dp)
                        } else {
                            Modifier.padding(bottom = 0.dp)
                        }
                    )
            )
        }
    }
}
