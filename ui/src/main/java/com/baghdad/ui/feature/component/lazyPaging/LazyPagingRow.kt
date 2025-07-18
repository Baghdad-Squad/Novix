package com.baghdad.ui.feature.component.lazyPaging

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> LazyPagingRow(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    key: ((item: T) -> Any)? = null,
    errorContent: @Composable () -> Unit = { DefaultErrorItem { items.retry() } },
    loadingContent: @Composable () -> Unit = { DefaultLoadingItem() },
    itemContent: @Composable (item: T) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
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
