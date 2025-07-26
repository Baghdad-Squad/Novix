package com.baghdad.ui.feature.component.lazyPaging

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> LazyPagingVerticalGrid(
    items: LazyPagingItems<T>,
    columns: GridCells,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    key: ((item: T) -> Any)? = null,
    errorContent: @Composable () -> Unit = { DefaultErrorItem { items.retry() } },
    loadingContent: @Composable () -> Unit = { DefaultLoadingItem() },
    state: LazyGridState = rememberLazyGridState(),
    itemContent: @Composable (item: T) -> Unit,
) {
    LazyVerticalGrid(
        state = state,
        columns = columns,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
    ) {
        items(
            count = items.itemCount,
            key = if (key != null) items.itemKey(key) else null
        ) { index ->
            val item = items[index] ?: return@items
            itemContent(item)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            AnimatedContent(items.loadState.append) { loadState ->
                when (loadState) {
                    is LoadState.Error -> errorContent()
                    LoadState.Loading -> loadingContent()
                    else -> {}
                }
            }
        }
    }
}