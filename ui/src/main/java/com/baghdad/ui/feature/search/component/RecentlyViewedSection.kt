package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.HorizontalMediaCardList
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun RecentlyViewedSection(
    recentViewed: List<SearchScreenState.RecentlyViewedUiState>,
    onClearRecentlyViewedClick: () -> Unit,
    onSavedClick: (Long) -> Unit,
    onRecentlyViewedClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionHeaderWithAction(
        modifier = modifier.padding(horizontal = 16.dp),
        title = stringResource(R.string.recent_viewed),
        onClearAllClick = {
            onClearRecentlyViewedClick()
        },
    )
    HorizontalMediaCardList(
        items = recentViewed,
        imageUrl = { it.posterPictureURL },
        onSavedClick = { onSavedClick(it.id) },
        onCardClick = { onRecentlyViewedClick(it.id, it.posterPictureURL) },
        isSaved = { it.isSaved },
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    )
}


