package com.baghdad.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.ActorCard

data class Actor(
    val name: String,
    val image: String
)

@Composable
fun ActorCardList(
    actors: List<Actor>,
    modifier: Modifier = Modifier

) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(900.dp)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(actors) { actor ->
            ActorCard(
                actorName = actor.name,
                actorImage = actor.image,
                )
        }
    }
}