package com.baghdad.ui.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.modifier.threeSidedBorder
import com.baghdad.design_system.preview.NovixPreviews
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.islamicImage.IslamicImage

@Composable
fun ActorCard(
    actorName: String,
    actorImage: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    characterName: String? = null
) {
    val isRTL = LocalLayoutDirection.current == LayoutDirection.Rtl
    val textContainerShape = RoundedCornerShape(12.dp)
    val imageShape = RoundedCornerShape(
        topEnd = 12.dp, topStart = 12.dp, bottomStart = 12.dp
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }) {
        IslamicImage(
            imageUrl = actorImage,
            contentDescription = stringResource(R.string.actor_image),
            modifier = Modifier
                .size(78.dp)
                .clip(imageShape)
                .border(1.dp, Theme.color.stroke, imageShape),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(min = 218.dp)
                .height(55.dp)
                .clip(textContainerShape)
                .threeSidedBorder(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    cornerRadius = 12.dp,
                    isRTL = isRTL
                )
                .background(Theme.color.surface)
                .padding(horizontal = 8.dp)
                .align(alignment = Alignment.Bottom)
        ) {
            Text(
                text = actorName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Theme.typography.title.medium.copy(color = Theme.color.body)
            )

            CharacterName(characterName)
        }
    }
}

@Composable
private fun CharacterName(characterName: String?) {
    if (!characterName.isNullOrBlank()) {
        Text(
            text = characterName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Theme.typography.label.small.copy(color = Theme.color.hint)
        )
    }
}

@NovixPreviews
@Composable
private fun ActorCardPreview() {
    NovixTheme(isDarkTheme = false) {
        Box(
            modifier = Modifier.background(Theme.color.surface)
        ) {
            ActorCard(
                actorName = "James Gandolfini",
                actorImage = "https://image.tmdb.org/t/p/w500/vhtsFJZcfHdeDkFBoWMDzOS6xrP.jpg",
                onClick = {},
                characterName = "Tony Soprano"
            )
        }
    }
}