package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme

private val CardHeight = 55.dp
private val ActorImageSize = 78.dp
private val RoundedShapeValue = 12.dp

private val ImageShape = RoundedCornerShape(
    topEnd = RoundedShapeValue, topStart = RoundedShapeValue, bottomStart = RoundedShapeValue
)
private val CardShape = RoundedCornerShape(
    topEnd = RoundedShapeValue,
    bottomEnd = RoundedShapeValue,
)

@Composable
fun ActorCard(
    actorName: String,
    actorImage: String,
    onClick: () -> Unit ,
    modifier: Modifier = Modifier,
    characterName: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Theme.color.surface)
            .noRippleClickable { onClick() }
    ) {
        AsyncImage(
            model = if (actorImage.isBlank()) null else actorImage,
            placeholder = painterResource(com.baghdad.islamic_image_loader.R.drawable.img_defualt_image),
            fallback = painterResource(com.baghdad.islamic_image_loader.R.drawable.img_defualt_image),
            contentDescription =
                if (characterName.isNullOrBlank())
                    "Portrait of actor $actorName"
                else
                    "Actor $actorName as $characterName",
            modifier = Modifier
                .size(ActorImageSize)
                .clip(ImageShape)
                .border(1.dp, Theme.color.stroke, ImageShape),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(min = 218.dp)
                .background(Theme.color.surface)
                .offset(x = (-1).dp)
                .border(
                    width = 1.dp, color = Theme.color.stroke, shape = CardShape
                )
                .clip(CardShape)
                .height(CardHeight)
                .padding(horizontal = 8.dp)
                .align(alignment = Alignment.Bottom)
        ) {
            Text(
                text = actorName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Theme.typography.title.medium.copy(color = Theme.color.body)
            )

            if (!characterName.isNullOrBlank()) {
                Text(
                    text = characterName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Theme.typography.title.small.copy(color = Theme.color.hint)
                )
            }
        }
    }
}