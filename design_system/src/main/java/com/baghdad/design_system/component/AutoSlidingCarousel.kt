package com.baghdad.design_system.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.baghdad.islamic_image_loader.component.SafeImage
import kotlinx.coroutines.delay

@Composable
fun AutoSlidingImageCarousel(
    imageUrls: List<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000L,
    imageAspectRatio: Float = 1.4f,
) {

    LaunchedEffect(imageUrls) {
        while (true) {
            delay(autoSlideDuration)
            val next = (pagerState.currentPage + 1) % imageUrls.size
            pagerState.animateScrollToPage(next)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(imageAspectRatio)
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
}





