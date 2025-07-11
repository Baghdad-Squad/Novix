package com.baghdad.feature.search.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.component.HorizontalDivider
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun RecentSearch(
    recentSearch: List<SearchScreenState.RecentSearchUiState>,
    onClearRecentSearchClick: () -> Unit,
    onRemoveRecentSearchItemClick: (Long) -> Unit
) {

    SectionHeaderWithAction(
        title = stringResource(R.string.recent_search),
        onClearAllClick = { onClearRecentSearchClick() })

    recentSearch.forEachIndexed { index, keyWord ->
        RecentSearchContent(
            title = keyWord.query,
            onCancelClick = { onRemoveRecentSearchItemClick(keyWord.id) },
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
