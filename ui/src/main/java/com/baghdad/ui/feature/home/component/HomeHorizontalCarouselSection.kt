package com.baghdad.ui.feature.home.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HorizontalCarousel
import com.baghdad.design_system.component.SectionHeader
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
    itemContent: @Composable (T , showSaveIcon : Boolean) -> Unit,
) {
    AnimatedVisibility(modifier = modifier, visible = isLoading || items.isNotEmpty()) {
        Crossfade(isLoading) { isLoading ->
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
                    HorizontalCarousel(
                        items = items,
                    ) { item , showSaveIcon ->
                        itemContent(item , showSaveIcon)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHorizontalCarouselLoadingPlaceHolder(modifier: Modifier = Modifier) {
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
        HorizontalCarousel(
            items = List(20) { },
        ) { item  , showSaveIcon->
            Box(
                modifier =
                    Modifier
                        .size(width = 158.dp, height = 210.dp)
                        .background(Theme.color.surface, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopRatingLoadingPlaceHolderPreview() {
    NovixTheme(isDarkTheme = true) {
        HomeHorizontalCarouselLoadingPlaceHolder()
    }
}
