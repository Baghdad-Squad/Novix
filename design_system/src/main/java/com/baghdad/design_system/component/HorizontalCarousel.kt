package com.baghdad.design_system.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
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
    shape : Shape = RoundedCornerShape(12.dp),
    state: CarouselState = rememberCarouselState { items.count() },
    itemContent: @Composable (T , showSaveIcon : Boolean) -> Unit
) {
//    val state = rememberCarouselState { items.count() }
//
//    HorizontalMultiBrowseCarousel(
//        state = state,
//        modifier = modifier
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        preferredItemWidth = largeItemWidth,
//        minSmallItemWidth = smallItemWidth,
//        maxSmallItemWidth = smallItemWidth,
//        itemSpacing = itemSpacing,
//        contentPadding = contentPadding
//    ) { i ->
//        val item = items[i]
//
//        Box(
//            modifier =Modifier
//                .fillMaxWidth()
//                .maskClip(shape)
//        ) {
//            itemContent(item, true)
//        }
//    }
    HeroCarousel(
        modifier = modifier,
        carouselState = state,
        heroItemSize = largeItemWidth,
        smallItemSize = smallItemWidth,
        itemSpacing = itemSpacing,
        contentPadding =contentPadding
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
