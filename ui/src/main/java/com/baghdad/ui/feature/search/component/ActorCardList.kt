package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.design_system.component.ActorCard
import com.baghdad.viewmodel.search.SearchScreenState


@Composable
fun ActorCardList(
    actors: LazyPagingItems<SearchScreenState.ActorUiState>,
    onActorClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(actors.itemCount) { index ->
            val actor = actors[index] ?: return@items
            ActorCard(
                actorName = actor.name,
                actorImage = actor.profilePictureURL,
                onClick = { onActorClick(actor.id) },
                )
        }
    }
}