package com.baghdad.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.carousel.CarouselState
import com.baghdad.design_system.component.carousel.rememberCarouselState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> HorizontalCarousel(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    itemSpacing: Dp = 8.dp,
    largeItemWidth: Dp = 158.dp,
    smallItemWidth: Dp = 74.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    state: CarouselState = rememberCarouselState { items.count() },
    itemContent: @Composable (T, showSaveIcon: Boolean) -> Unit
) {
    HeroCarousel(
        modifier = modifier,
        carouselState = state,
        heroItemSize = largeItemWidth,
        smallItemSize = smallItemWidth,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding
    ) { index ->
        val item = items[index]

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maskClip(shape)
        ) {
            itemContent(item, true)
        }

    }
}
