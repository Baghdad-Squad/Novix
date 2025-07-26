package com.baghdad.ui.feature.tvShowDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.ActorCard
import com.baghdad.viewmodel.tvShowDetails.TvShowDetailsScreenState

@Composable
fun CastMembersSection(
    actors: List<TvShowDetailsScreenState.CastMemberUiState>,
    onClickCastMember: (actorId: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.cast),
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(actors) {
                ActorCard(
                    actorImage = it.imageUrl,
                    actorName = it.name,
                    characterName = it.characterName,
                    onClick = { onClickCastMember(it.id) }
                )
            }
        }
    }
}

