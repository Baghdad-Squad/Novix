package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.design_system.component.ActorCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingColumn
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingColumn
import com.baghdad.viewmodel.search.SearchScreenState


@Composable
fun ActorCardList(
    actors: LazyPagingItems<SearchScreenState.ActorUiState>,
    onActorClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyPagingColumn(
        modifier = modifier,
        items = actors,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        key = { it.id },
        contentPadding = PaddingValues(vertical = 8.dp)
    ) { actor ->
        ActorCard(
            actorName = actor.name,
            actorImage = actor.profilePictureURL,
            onClick = { onActorClick(actor.id) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}