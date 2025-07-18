package com.baghdad.ui.feature.movieDetails.component

import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baghdad.design_system.component.ActorCard
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.movieDetails.MovieDetailsState

@Composable
fun ActorsSection(
    actors: List<MovieDetailsState.ActorCardInfo>,
    modifier: Modifier = Modifier,
    onClick: (id: Long) -> Unit = {}
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Text(
            text = stringResource(com.baghdad.ui.R.string.cast),
            fontSize = 18.sp,
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(actors) { actor ->
                actor.imageUrl?.let {
                    ActorCard(
                        actorName = actor.name,
                        actorImage = it,
                        onClick = {onClick(actor.id.toLong())},
                        characterName = actor.characterName,
                        modifier = Modifier.fillParentMaxWidth(0.92f)
                    )
                }

            }
        }
    }
}