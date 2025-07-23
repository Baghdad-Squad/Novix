package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.islamicImage.IslamicImage
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme

@Composable
fun PopularCard(
    contentName: String,
    contentRating: Double,
    imageUrl: String,
    onCardClick: () -> Unit,
    onSavedClick: () -> Unit,
    isSaved: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(width = 188.dp, height = 280.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.color.surface, shape = RoundedCornerShape(12.dp))
            .border(1.dp, Theme.color.stroke, shape = RoundedCornerShape(12.dp))
            .noRippleClickable { onCardClick() },
        contentAlignment = Alignment.Center,

        ) {
        IslamicImage(
            imageUrl = imageUrl,
            contentDescription = contentName,
            modifier = Modifier
                .fillMaxSize()
                .align(alignment = Alignment.Center)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0x4D0D0608),
                                Color(0x990D0608)
                            ),
                        )
                    )
                },
        )
        SaveIcon(
            isSaved = isSaved,
            onClick = { onSavedClick() },
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopStart)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = contentName,
                style = Theme.typography.label.medium,
                color = Theme.color.onPrimary,
            )
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_rating_star),
                    contentDescription = stringResource(R.string.rating_star),
                    tint = Theme.color.yellowAccent,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = contentRating.toString(),
                    style = Theme.typography.label.small,
                    color = Theme.color.onPrimary
                )
            }
        }
    }
}

@NovixPreviews
@Composable
private fun PopularCardPrev() {
    NovixTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Theme.color.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PopularCard(
                contentName = "The Shawshank Redemption",
                contentRating = 9.9,
                imageUrl = "https://image.tmdb.org/t/p/w500/etT14XfDEqhQZdD47ywpyihXPyW.jpg",
                onCardClick = {},
                onSavedClick = {},
                isSaved = false,
            )
        }
    }
}