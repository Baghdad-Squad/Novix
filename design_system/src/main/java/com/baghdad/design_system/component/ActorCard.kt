package com.baghdad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.modifier.Side
import com.baghdad.design_system.modifier.customBorder
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage

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
        SafeImage(
            imageUrl = actorImage,
            contentDescription =
                if (characterName.isNullOrBlank())
                    "Portrait of actor $actorName"
                else
                    "Actor $actorName as $characterName",
            modifier = Modifier
                .size(ActorImageSize)
                .clip(ImageShape)
                .border(1.dp, Theme.color.stroke, ImageShape),
            blur = 12.dp,
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.color.surface)
                .customBorder(
                    borderWidth = 1.dp,
                    color = Theme.color.stroke,
                    sides = listOf(Side.Top, Side.Bottom)
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

@Composable
@Preview(showBackground = true)
private fun ActorCardPreview(){
    ActorCard(
        actorName = "Hasan",
        actorImage = "https://example.com/image.jpg",
        onClick = { },
    )
}

