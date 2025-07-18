package com.baghdad.design_system.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoSlidingImageCarousel(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000L,
    indicatorVisibility: Boolean = true,
) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (imageUrls.isNotEmpty()) {
            delay(autoSlideDuration)
            val nextPage = (pagerState.currentPage + 1) % imageUrls.size
            coroutineScope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)),
        ) { page ->
            SafeImage(
                contentScale = ContentScale.Crop,
                imageUrl = imageUrls[page],
                contentDescription = "Image $page",
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0x4D000000),
                                    Color(0x14000000)
                                ),
                            )
                        )
                    }
            )
        }

        AnimatedVisibility(
            visible = indicatorVisibility,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Row(
                modifier = Modifier

                    .clip(RoundedCornerShape(8.dp))
                    .background(Theme.color.iconBackgroundLow)
                    .border(
                        width = 1.dp,
                        color = Theme.color.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                CarousalDot(
                    totalDots = imageUrls.size,
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier
                )
            }
        }
    }
}





