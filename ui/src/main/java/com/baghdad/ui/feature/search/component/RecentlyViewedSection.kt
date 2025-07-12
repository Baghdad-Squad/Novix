package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun RecentlyViewedSection(
    recentViewed: List<SearchScreenState.MediaUiState>,
    onClearRecentlyViewedClick: () -> Unit,
    onSavedClick: (Long) -> Unit,
    onRecentlyViewedClick: (Long) -> Unit,
    modifier: Modifier=Modifier
) {
    SectionHeaderWithAction(
        modifier = modifier,
        title = stringResource(R.string.recent_viewed),
        onClearAllClick = onClearRecentlyViewedClick,
    )
    RecentlyViewedList(
        recentViewed = recentViewed,
        onSavedClick = onSavedClick,
        onRecentlyViewedClick = onRecentlyViewedClick,
        modifier = Modifier
            .fillMaxWidth(0.45f)
            .aspectRatio(0.8f)
            .padding(bottom = 12.dp)
    )

}


