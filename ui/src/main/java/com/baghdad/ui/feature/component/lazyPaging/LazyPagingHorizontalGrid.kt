package com.baghdad.ui.feature.component.lazyPaging

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> LazyPagingHorizontalGrid(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    rows: GridCells = GridCells.Adaptive(minSize = 150.dp),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    key: ((item: T) -> Any)? = null,
    errorContent: @Composable () -> Unit = { DefaultErrorItem { items.retry() } },
    loadingContent: @Composable () -> Unit = { DefaultLoadingItem() },
    itemContent: @Composable (item: T) -> Unit,
) {
    LazyHorizontalGrid(
        rows = rows,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement
    ) {
        items(
            count = items.itemCount,
            key = if (key != null) items.itemKey(key) else null
        ) { index ->
            val item = items[index] ?: return@items
            itemContent(item)
        }

        item {
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
