package com.baghdad.ui.feature.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.baghdad.ui.feature.component.islamicImage.IslamicImage
import kotlinx.coroutines.delay

@Composable
fun AutoSlidingImageCarousel(
    imageUrls: List<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000L,
    imageAspectRatio: Float = 1.4f,
) {
    if (imageUrls.isNotEmpty()) {
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
            IslamicImage(
                imageUrl = imageUrls[page],
                contentScale = ContentScale.Crop,
                contentDescription = "Image $page",
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush =
                                Brush.verticalGradient(
                                    colors =
                                        listOf(
                                            Color(0x99000000),
                                            Color(0x00000000)
                                ),
                                        )
                        )
                    }
            )
        }
    } else {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(imageAspectRatio)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                Color(0x99000000),
                                Color(0x00000000)
                            ),
                                )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(com.baghdad.ui.R.drawable.img_defualt_image),
                contentDescription = "Default Image",
                modifier = Modifier.size(56.dp)
            )
        }
    }
}





