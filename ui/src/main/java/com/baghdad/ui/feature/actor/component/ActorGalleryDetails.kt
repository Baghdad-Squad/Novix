package com.baghdad.ui.feature.actor.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.design_system.theme.Theme

@Composable
fun ActorGalleryDetails(
    actorPhotos: List<Int>,
    modifier: Modifier = Modifier,
    onClickShowAll: () -> Unit = {}
) {
    Column {
        SectionHeader(
            title = "Gallery",
            isShowAllVisiable = true,
            onClick = onClickShowAll,
            modifier = Modifier.fillMaxWidth()
        )

        LazyRow(modifier = modifier.padding(top = 12.dp, start = 16.dp)) {

            items(actorPhotos.size) { photo ->
                Image(
                    painter = painterResource(photo),
                    contentDescription = "Actor Image",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(88.dp)
                        .clip(RoundedCornerShape(12))
                        .border(1.dp, Theme.color.stroke, RoundedCornerShape(12))
                )
            }
        }
    }
}