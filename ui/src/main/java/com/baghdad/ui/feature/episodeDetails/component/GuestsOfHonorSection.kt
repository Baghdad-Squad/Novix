package com.baghdad.ui.feature.episodeDetails.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.baghdad.design_system.component.ActorCard
import com.baghdad.viewmodel.episodeDetails.EpisodeDetailsScreenState


fun LazyListScope.guestsOfHonorItems(
    guestsOfHonor: List<EpisodeDetailsScreenState.GuestsOfHonerUiState>,
    onClick: (Long) -> Unit,
) {
    items(
        items = guestsOfHonor,
        key = { it.id }
    ) { guestOfHonor ->
        ActorCard(
            actorName = guestOfHonor.name,
            actorImage = guestOfHonor.profilePictureURL,
            onClick = {
                onClick(guestOfHonor.id)
            },
            characterName = guestOfHonor.characterName,
            modifier = Modifier.animateItem()
        )
    }
}