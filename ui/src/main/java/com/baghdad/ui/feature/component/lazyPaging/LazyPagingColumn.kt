package com.baghdad.ui.feature.component.lazyPaging

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> LazyPagingColumn(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    key: ((item: T) -> Any)? = null,
    errorContent: @Composable () -> Unit = { DefaultErrorItem { items.retry() } },
    loadingContent: @Composable () -> Unit = { DefaultLoadingItem() },
    itemContent: @Composable (item: T) -> Unit,
) {
    LazyColumn(
        state = state,
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
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