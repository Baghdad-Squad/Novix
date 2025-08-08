package com.baghdad.ui.feature.myLists.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.viewmodel.myLists.MyListsScreenState

@Composable
fun SavedListsVerticalGrid(
    savedLists: LazyPagingItems<MyListsScreenState.SavedListUiState>,
    onSavedListClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyPagingVerticalGrid(
        modifier = modifier,
        items = savedLists,
        columns = GridCells.Adaptive(minSize = 328.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) { savedList ->
        SavedListItem(
            name = savedList.name,
            itemCount = savedList.itemCount,
            onClick = { onSavedListClick(savedList.id) },
        )
    }
}
