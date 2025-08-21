package com.baghdad.ui.feature.home.component

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.design_system.component.carousel.CarouselState
import com.baghdad.design_system.component.carousel.HeroCarousel
import com.baghdad.design_system.component.carousel.rememberCarouselState
import com.baghdad.design_system.modifier.shimmerEffect
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun <T> HomeHorizontalCarouselSection(
    title: String,
    isLoading: Boolean,
    items: List<T>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    carouselState: CarouselState = rememberCarouselState { items.size },
    itemContent: @Composable (item: T, modifier: Modifier) -> Unit,
) {
    Crossfade(modifier = modifier, targetState = isLoading) { isLoading ->
        if (isLoading) {
            HomeHorizontalCarouselLoadingPlaceHolder()
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SectionHeader(
                    title = title,
                    isShowAllVisible = true,
                    onClick = onViewAllClick,
                )
                HeroCarousel(
                    modifier = Modifier
                        .height(HomeCarouselDefaults.CAROUSEL_HEIGHT)
                        .padding(start = HomeCarouselDefaults.CAROUSEL_START_PADDING),
                    carouselState = carouselState,
                    heroItemSize = HomeCarouselDefaults.HERO_ITEM_SIZE,
                    smallItemSize = HomeCarouselDefaults.SMALL_ITEM_SIZE,
                    itemSpacing = HomeCarouselDefaults.ITEM_SPACING,
                    contentPadding = PaddingValues(end = HomeCarouselDefaults.CONTENT_END_PADDING)
                ) { index ->
                    val item = items[index]
                    itemContent(
                        item,
                        Modifier
                            .maskClip(RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS))
                        .maskBorder(
                            border = BorderStroke(
                                width = HomeCarouselDefaults.BORDER_WIDTH,
                                color = Theme.color.stroke
                            ),
                            shape = RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHorizontalCarouselLoadingPlaceHolder(modifier: Modifier = Modifier) {
    val state = rememberCarouselState { 10 }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(width = 166.dp, height = 30.dp)
                        .background(Theme.color.surface, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
            )
            Box(
                modifier =
                    Modifier
                        .size(width = 46.dp, height = 24.dp)
                        .background(Theme.color.surface, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
            )
        }
        HeroCarousel(
            modifier = Modifier
                .height(HomeCarouselDefaults.CAROUSEL_HEIGHT)
                .padding(start = HomeCarouselDefaults.CAROUSEL_START_PADDING),
            carouselState = state,
            heroItemSize = HomeCarouselDefaults.HERO_ITEM_SIZE,
            smallItemSize = HomeCarouselDefaults.SMALL_ITEM_SIZE,
            itemSpacing = HomeCarouselDefaults.ITEM_SPACING,
            contentPadding = PaddingValues(end = HomeCarouselDefaults.CONTENT_END_PADDING)
        ) {
            Box(
                modifier =
                    Modifier
                        .size(width = HomeCarouselDefaults.HERO_ITEM_SIZE, height = HomeCarouselDefaults.CAROUSEL_HEIGHT)
                        .background(Theme.color.surface, RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS))
                        .maskClip(RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS))
                        .maskBorder(
                            border = BorderStroke(
                                width = HomeCarouselDefaults.BORDER_WIDTH,
                                color = Theme.color.stroke
                            ),
                            shape = RoundedCornerShape(HomeCarouselDefaults.CARD_CORNER_RADIUS)
                        )
                        .shimmerEffect(),
            )
        }
    }
}

private object HomeCarouselDefaults {
    val CONTENT_END_PADDING = 16.dp
    val CAROUSEL_START_PADDING = 16.dp
    val CAROUSEL_HEIGHT = 210.dp
    val HERO_ITEM_SIZE = 158.dp
    val SMALL_ITEM_SIZE = 74.dp
    val ITEM_SPACING = 8.dp
    val CARD_CORNER_RADIUS = 12.dp
    val BORDER_WIDTH = 1.dp
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopRatingLoadingPlaceHolderPreview() {
    NovixTheme(isDarkTheme = true) {
        HomeHorizontalCarouselLoadingPlaceHolder()
    }
}
