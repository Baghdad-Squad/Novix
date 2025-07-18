package com.baghdad.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.ActorCard
import com.baghdad.viewmodel.search.SearchScreenState


@Composable
fun ActorCardList(
    actors: List<SearchScreenState.ActorUiState>,
    onActorClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(actors) { actor ->
            ActorCard(
                actorName = actor.name,
                actorImage = actor.profilePictureURL,
                onClick = { onActorClick(actor.id) },
            )
        }
    }
}