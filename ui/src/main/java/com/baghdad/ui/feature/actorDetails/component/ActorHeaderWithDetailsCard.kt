package com.baghdad.ui.feature.actorDetails.component

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
import com.baghdad.viewmodel.actorDetails.ActorDetailsScreenState

@Composable
fun ActorHeaderWithDetailsCard(
    uiState: ActorDetailsScreenState,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { uiState.actorInfo.headerPictures.size })

    Box(modifier = modifier) {
        AutoSlidingImageCarousel(
            imageUrls = uiState.actorInfo.headerPictures,
            pagerState = pagerState,
            imageAspectRatio = 1.778f,
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 84.dp)
        ) {
            if (uiState.actorInfo.headerPictures.size > 1) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Theme.color.iconBackgroundLow)
                        .border(1.dp, Theme.color.stroke, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CarousalDot(
                        totalDots = uiState.actorInfo.headerPictures.size,
                        selectedIndex = pagerState.currentPage
                    )
                }
            }

            ActorCardDetails(
                fullName = uiState.actorInfo.name,
                characterRole = uiState.actorInfo.department,
                birthPlace = uiState.actorInfo.placeOfBirth,
                birthDate = uiState.actorInfo.birthdayDate,
                deathDate = uiState.actorInfo.deathDate,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
