package com.baghdad.feature.search.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HorizontalDivider
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchScreenState

fun LazyListScope.recentSearchSection(
    recentSearch: List<SearchScreenState.RecentSearchUiState>,
    onClearRecentSearchClick: () -> Unit,
    onRecentSearchClicked: (Long) -> Unit,
    onRemoveRecentSearchItemClick: (Long) -> Unit
) {
    item {
        SectionHeaderWithAction(
            title = stringResource(R.string.recent_search),
            onClearAllClick = { onClearRecentSearchClick() })
    }

    itemsIndexed(recentSearch) { index, keyWord ->
        RecentSearchItem(
            title = keyWord.query,
            onCancelClick = { onRemoveRecentSearchItemClick(keyWord.id) },
            onRecentSearchClicked = { onRecentSearchClicked(keyWord.id) }
        )

        if (index < recentSearch.lastIndex) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 2.dp),
                thickness = 1.dp,
                color = Theme.color.stroke
            )
        }
    }
}
