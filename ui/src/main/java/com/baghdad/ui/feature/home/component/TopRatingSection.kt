
package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HorizontalImageCarousel
import com.baghdad.islamic_image_loader.component.SafeImage

@Composable
fun TopRatingSection(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    HorizontalImageCarousel(
        itemCount = imageUrls.size
    ) { index ->
        AsyncImage(
            model = imageUrls[index],
            contentDescription = "Image $index",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}