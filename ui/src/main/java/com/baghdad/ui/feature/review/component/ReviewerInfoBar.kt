package com.baghdad.ui.feature.review.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage

private val ReviewerImageSize = 48.dp
private val ReviewerBarPadding = 12.dp
private val ContentStartPadding = 8.dp

@Composable
fun ReviewerHeader(
    imageUrl: String,
    name: String,
    contentName: String,
    rate: Float,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(ReviewerBarPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        SafeImage(
            imageUrl = imageUrl,
            contentDescription = "Reviewer image for $name",
            modifier = Modifier
                .size(ReviewerImageSize)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = ContentStartPadding)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = name,
                style = Theme.typography.title.medium,
                color = Theme.color.title,
                maxLines = 1
            )
            Text(
                text = contentName,
                style = Theme.typography.label.small,
                color = Theme.color.hint,
                maxLines = 1
            )
        }

        ReviewerRating(rating = rate, modifier = Modifier)

    }
}