package com.baghdad.ui.feature.episodeDetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.feature.component.ActorCard
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenState


fun LazyListScope.guestsOfHonorItems(
    guestsOfHonor: List<EpisodeDetailsScreenState.GuestsOfHonerUiState>,
    onClick: (Long) -> Unit,
) {
    item {
        AnimatedVisibility(guestsOfHonor.isNotEmpty()) {
            Text(
                text = stringResource(com.baghdad.ui.R.string.guests_of_honor),
                style = Theme.typography.title.medium,
                color = Theme.color.title,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 9.dp)
            )
        }
    }
    items(
        items = guestsOfHonor,
    ) { guestOfHonor ->
        ActorCard(
            actorName = guestOfHonor.name,
            actorImage = guestOfHonor.profilePictureURL,
            onClick = {
                onClick(guestOfHonor.id)
            },
            characterName = guestOfHonor.characterName,
            modifier = Modifier
                .animateItem()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp)
        )
    }
}