package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import com.baghdad.ui.R
import com.baghdad.ui.feature.gallery.GalleryScreen

@Composable
fun GallerySection(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    onClickShowAll: () -> Unit = {}
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = stringResource(R.string.gallery),
            isShowAllVisiable = true,
            onClick = onClickShowAll,
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 12.dp, start = 16.dp)
        ) {

            items(imageUrls) {
                SafeImage(
                    imageUrl = it,
                    contentDescription = stringResource(R.string.gallery),
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(12))
                        .border(1.dp, Theme.color.stroke, RoundedCornerShape(12)),
                )
            }
        }
    }
}

