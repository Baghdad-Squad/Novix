package com.baghdad.ui.feature.tvShowDetails.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.baghdad.design_system.modifier.noRippleClickable
import com.baghdad.design_system.theme.Theme
import com.baghdad.islamic_image_loader.component.SafeImage
import com.baghdad.ui.R
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenState

@Composable
fun EpisodesSection(
    episodes: List<TvShowDetailsScreenState.EpisodeUiState>,
    onClickEpisode: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        episodes.forEach { episode ->
            EpisodeCard(
                episodeNumber = episode.episodeNumber,
                imageUrl = episode.imageUrl,
                episodeName = episode.name,
                releaseDate = episode.releaseDate,
                duration = episode.duration,
                rating = episode.rating,
                modifier = Modifier
                    .then(
                        if (episode != episodes.last()) {
                            Modifier.padding(bottom = 8.dp)
                        } else {
                            Modifier
                        }
                    )
                    .noRippleClickable {
                        onClickEpisode(episode.currentSeason, episode.episodeNumber)
                    }
            )
        }
    }
}

@Composable
fun EpisodeCard(
    episodeNumber: Int,
    imageUrl: String,
    episodeName: String,
    releaseDate: String,
    duration: String,
    rating: Double,
    modifier: Modifier = Modifier
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
                .weight(1f)
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
                .fillMaxWidth()
                .weight(2f),
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
                    title = duration.toString(),
                    icon = painterResource(com.baghdad.design_system.R.drawable.ic_clock),
                    tint = Theme.color.hint
                )

                CircleDot(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = releaseDate.toString(),
                    style = Theme.typography.label.small,
                    color = Theme.color.hint
                )
            }
        }
    }
}