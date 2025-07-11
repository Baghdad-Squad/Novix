package com.baghdad.ui.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.ui.feature.search.component.SearchTextField
import com.baghdad.ui.feature.search.component.recentSearchSection
import com.baghdad.ui.feature.search.component.RecentlyViewedSection
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
            SearchTextField(
                query = uiState.searchText,
                onQueryChange = { listener.onSearchTextChanged(it) },
                onFilterIconClick = { listener.onFilterIconClick() }
            )
        }
        item {
            RecentlyViewedSection(
               uiState.recentViewed,
               onClearRecentlyViewedClick =  {listener.onClearRecentSearchClick()},
                onSavedClick = { onSavedClick(it) }
            )
        }
            recentSearchSection(
                recentSearch = uiState.recentSearch,
                onClearRecentSearchClick = { listener.onClearRecentSearchClick() },
                onRemoveRecentSearchItemClick = { listener.onRemoveRecentSearchItemClick(it) },
                onRecentSearchClicked = { listener.onRecentSearchItemClick(it) }
            )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchScreenPreview() {

}