package com.baghdad.ui.feature.tvShowDetails.component

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
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsInteractionListener
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenState


@Composable
fun TvShowHeaderWithDetailsCard(
    uiState: TvShowDetailsScreenState,
    listener: TvShowDetailsInteractionListener,
    modifier: Modifier = Modifier
) {
    val images = if (uiState.tvShowInfo.headerImagesURLs.isEmpty())  listOf(uiState.tvShowInfo.posterPictureURL)  else  uiState.tvShowInfo.headerImagesURLs
    val pagerState = rememberPagerState(pageCount = { images.size })
    val aspectRatio = if (uiState.tvShowInfo.headerImagesURLs.isNotEmpty()) 1.778f  else  1.2f


    Box(modifier = modifier) {
        AutoSlidingImageCarousel(
            imageUrls = images,
            imageAspectRatio = aspectRatio,
            pagerState = pagerState
        )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 128.dp)
                    .then(Modifier.padding(top = if (uiState.tvShowInfo.genres.isEmpty()) 24.dp else 0.dp))
            ) {
                if (uiState.tvShowInfo.headerImagesURLs.size > 1) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Theme.color.iconBackgroundLow)
                            .border(1.dp, Theme.color.stroke, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CarousalDot(
                            totalDots = uiState.tvShowInfo.headerImagesURLs.size,
                            selectedIndex = pagerState.currentPage,
                            modifier = Modifier
                        )
                    }
                }


                TvShowDetailsCard(
                    tvShowId = tvShowId,
                    title = uiState.tvShowInfo.title,
                    genres = uiState.tvShowInfo.genres,
                    rating = uiState.tvShowInfo.rating,
                    date = uiState.tvShowInfo.releaseDate,
                    seasonsCount = uiState.tvShowInfo.seasonCount,
                    onReviewClick = { listener.onClickReviews(tvShowId) },
                    onGenreClick = { genreId ->
                        genreId?.let { listener.onClickGenre(it) }
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
