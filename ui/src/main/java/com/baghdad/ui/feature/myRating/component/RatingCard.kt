package com.baghdad.ui.feature.myRating.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.DeleteChip
import com.baghdad.design_system.component.RatingChip
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.islamicImage.IslamicImage

@Composable
fun RatingCard(
    url: String,
    rating: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(158.dp, 210.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.color.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.color.stroke, shape = RoundedCornerShape(12.dp))
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
        ) {
        IslamicImage(
            imageUrl = url,
            contentDescription = contentDescription,
            loadingContent = null,
            modifier = Modifier
                .fillMaxSize()
                .align(alignment = Alignment.Center)
        )
        RatingChip(
            rating.toString(),
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
        )
        DeleteChip(
            onClick = { onDeleteClick() },
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.TopEnd)
        )
    }
}

@Preview
@Composable
private fun RatingCardPrev() {
    RatingCard(
        url = "",
        rating = 10,
        contentDescription = "hi",
        onClick = {},
        onDeleteClick = {},
    )
}