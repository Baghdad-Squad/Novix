package com.baghdad.ui.feature.actorDetails.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.ActorImageDialog
import com.baghdad.ui.feature.component.islamicImage.IslamicImage

@Composable
fun GallerySection(
    imageUrls: List<String>,
    isShowAllVisible: Boolean,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClickShowAll: () -> Unit = {}
) {
    Column(modifier = modifier) {
        SectionHeader(
            title = stringResource(R.string.gallery),
            isShowAllVisible = isShowAllVisible,
            onClick = onClickShowAll,
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            items(imageUrls) {
                IslamicImage(
                    imageUrl = it,
                    contentDescription = stringResource(R.string.gallery),
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(12))
                        .border(1.dp, Theme.color.stroke, RoundedCornerShape(12))
                        .clickable { onImageClick(it) },
                )
            }
        }
    }
}

