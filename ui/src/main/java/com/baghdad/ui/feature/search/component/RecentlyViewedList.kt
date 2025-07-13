package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HomeCard
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun RecentlyViewedList(
    recentViewed: List<SearchScreenState.RecentlyViewedUiState>,
    onSavedClick: (Long) -> Unit,
    onRecentlyViewedClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            key = { it.id },
            items = recentViewed
        ) { recentViewedItem ->
            HomeCard(
                url = recentViewedItem.posterPictureURL,
                contentDescription = recentViewedItem.id.toString(),
                isSaved = recentViewedItem.isSaved,
                onSavedClick = { onSavedClick(recentViewedItem.id) },
                onClick = {onRecentlyViewedClick(recentViewedItem.id) },
                modifier = Modifier.animateItem()
            )
        }
    }
}