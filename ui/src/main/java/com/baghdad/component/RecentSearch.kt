package com.baghdad.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.search.SearchInteractionListener

@Composable
fun RecentSearch(
    recentSearch: List<String>,
    listener: SearchInteractionListener
) {
    SectionHeaderWithAction(
        title = stringResource(R.string.recent_search),
        onClearAllClick = { listener.onClearRecentSearchClick() })

    recentSearch.forEachIndexed { index, title ->
        RecentSearchContent(
            title = title,
            onCancelClick = { listener.onRemoveRecentSearchItemClick(index.toLong()) },
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
