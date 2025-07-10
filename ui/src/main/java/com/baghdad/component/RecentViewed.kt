package com.baghdad.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
 fun RecentViewedSection(
    uiState: SearchScreenState,
    listener: SearchInteractionListener,
    onMovieSavedClick: (Long) -> Unit
) {
    val recentViewed = uiState.recentViewed
    SectionHeaderWithAction(
        title = stringResource(R.string.recent_viewed),
        onClearAllClick = { listener.onClearRecentWatchingClick() }
    )

    recentViewed.forEach { mediaItem ->
        MoviesCard(
            url = mediaItem.posterPictureURL,
            contentDescription = mediaItem.id.toString(),
            isSaved = mediaItem.isSaved,
            onSavedClick = { onMovieSavedClick(mediaItem.id) },
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .aspectRatio(0.8f)
                .padding(bottom = 12.dp)
        )
    }
}
