package com.baghdad.ui.feature.episodeDetails.component

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
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsInteractionListener
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenState

@Composable
fun EpisodeHeaderWithDetailsCard(
    state: EpisodeDetailsScreenState,
    listener: EpisodeDetailsInteractionListener,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { state.episode.headerPictures.size })

    Box(modifier = modifier) {
        AutoSlidingImageCarousel(
            imageUrls = state.episode.headerPictures,
            imageAspectRatio = 1.778f,
            pagerState = pagerState
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 82.dp)
        ) {
            if (state.episode.headerPictures.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Theme.color.iconBackgroundLow)
                        .border(1.dp, Theme.color.stroke, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CarousalDot(
                        totalDots = state.episode.headerPictures.size,
                        selectedIndex = pagerState.currentPage,
                        modifier = Modifier
                    )
                }
            }

            EpisodeDetailsHeader(
                title = "Episode ${state.episode.episodeNumber} - ${state.episode.title}",
                releaseDate = state.episode.releasedDate,
                rating = state.episode.rating,
                categories = state.episode.categories,
                onCategoryClicked = { listener.onCategoryClick(it) },
                seasonNumber = state.episode.currentSeason,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
