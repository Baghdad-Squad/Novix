package com.baghdad.ui.feature.tvShowDetails.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.CircleDot
import com.baghdad.design_system.component.LabeledIconRow
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import com.baghdad.ui.R

@Composable
fun EpisodeCard(
    episodeNumber: Int,
    imageUrl: String,
    episodeName: String,
    releaseDate: String,
    duration: String,
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SafeImage(
            imageUrl = imageUrl,
            contentDescription = episodeName,
            modifier = Modifier
                .width(116.dp)
                .height(78.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Theme.color.stroke,
                    shape = RoundedCornerShape(12.dp)
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = stringResource(R.string.episode, episodeNumber),
                style = Theme.typography.label.large,
                color = Theme.color.title
            )
            Text(
                text = episodeName,
                style = Theme.typography.label.small,
                color = Theme.color.hint
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                LabeledIconRow(
                    title = rating.toString(),
                    icon = painterResource(com.baghdad.design_system.R.drawable.ic_star_filled),
                    tint = Theme.color.yellowAccent
                )

                CircleDot(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                LabeledIconRow(
                    title = duration,
                    icon = painterResource(com.baghdad.design_system.R.drawable.ic_clock),
                    tint = Theme.color.hint
                )

                CircleDot(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = releaseDate,
                    style = Theme.typography.label.small,
                    color = Theme.color.hint
                )
            }
        }
    }
}