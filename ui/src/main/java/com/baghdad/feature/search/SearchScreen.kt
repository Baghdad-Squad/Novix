package com.baghdad.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.feature.search.component.RecentSearch
import com.baghdad.feature.search.component.RecentViewedSection
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun SearchScreen(
    listener: SearchInteractionListener,
    uiState: SearchScreenState,
    onSavedClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        item {
            RecentViewedSection(
               uiState.recentViewed,
               onClearRecentSearchClick =  {listener.onClearRecentSearchClick()},
                onSavedClick = { onSavedClick(it) }
            )
        }
        item {
            RecentSearch(
                recentSearch = uiState.recentSearch,
                onClearRecentSearchClick = { listener.onClearRecentSearchClick() },
                onRemoveRecentSearchItemClick = { listener.onRemoveRecentSearchItemClick(it) })
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchScreenPreview() {

}