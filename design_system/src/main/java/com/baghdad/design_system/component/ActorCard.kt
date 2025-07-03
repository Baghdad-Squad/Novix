package com.baghdad.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme

private val CardHeight = 55.dp
private val ContentStartPadding = 86.dp
private val ActorImageSize = 78.dp
private val RoundedShapeValue = 12.dp
private val ImageShape = RoundedCornerShape(
    topEnd = RoundedShapeValue,
    topStart = RoundedShapeValue,
    bottomStart = RoundedShapeValue
)
private val CardShape = RoundedCornerShape(
    topEnd = RoundedShapeValue,
    bottomEnd = RoundedShapeValue,
    bottomStart = RoundedShapeValue
)

@Composable
fun ActorCard(
    actorName: String,
    actorImage: Painter,
    modifier: Modifier = Modifier,
    characterName: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Theme.color.surface)
    ) {

        Box(
            modifier = Modifier
                .height(CardHeight)
                .align(Alignment.BottomEnd)
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = CardShape
                )
                .background(color = Theme.color.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = ContentStartPadding, end = 12.dp)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = actorName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Theme
                        .typography
                        .title
                        .medium
                        .copy(color = Theme.color.body),
                    modifier = Modifier.fillMaxWidth()
                )

                if (!characterName.isNullOrBlank()) {
                    Text(
                        text = characterName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Theme
                            .typography
                            .title
                            .medium
                            .copy(color = Theme.color.hint),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Image(
            painter = actorImage,
            contentScale = ContentScale.Crop,
            contentDescription =
                if (characterName.isNullOrBlank()) "Portrait of actor $actorName"
                else "Actor $actorName as $characterName",
            modifier = Modifier
                .size(ActorImageSize)
                .align(Alignment.CenterStart)
                .clip(ImageShape)
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = ImageShape
                )
        )
    }
}