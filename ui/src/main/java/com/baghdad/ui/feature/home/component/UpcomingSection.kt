package com.baghdad.ui.feature.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.lazyPaging.DefaultErrorItem
import com.baghdad.ui.feature.component.lazyPaging.DefaultLoadingItem
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState

fun LazyGridScope.upcomingSection(
    selectedGenreId: Long,
    genres: List<GenreUiState>,
    isGenresLoading: Boolean,
    onGenreSelected: (GenreUiState) -> Unit,
    upcomingItems: LazyPagingItems<UpcomingItemUiState>,
    onUpcomingItemClicked: (UpcomingItemUiState) -> Unit,
    onUpcomingItemSaveClicked: (UpcomingItemUiState) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        UpcomingSectionHeader(
            genres = genres,
            isGenresLoading = isGenresLoading,
            onGenreSelected = onGenreSelected,
            selectedGenreId = selectedGenreId
        )
    }
    items(
        count = upcomingItems.itemCount,
        key = upcomingItems.itemKey { it.id }
    ) { index ->
        val item = upcomingItems[index] ?: return@items
        HomeCard(
            url = item.imageUrl,
            contentDescription = null,
            isSaved = item.isSaved,
            onSavedClick = { onUpcomingItemSaveClicked(item) },
            onClick = {
                onUpcomingItemClicked(item)
            },
            modifier = Modifier.aspectRatio(0.8f)
        )
    }
    item(span = { GridItemSpan(maxLineSpan) }) {
        AnimatedContent(upcomingItems.loadState.append) { loadState ->
            when (loadState) {
                is LoadState.Error -> DefaultErrorItem { upcomingItems.retry() }
                LoadState.Loading -> DefaultLoadingItem()
                else -> {}
            }
        }
    }
}

@Composable
fun UpcomingSectionHeader(
    selectedGenreId: Long,
    genres: List<GenreUiState>,
    isGenresLoading: Boolean,
    onGenreSelected: (GenreUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        SectionHeader(
            title = stringResource(R.string.upcoming),
            isShowAllVisible = false,
            modifier = modifier.wrapContentSize()
        )
        LazyRow(
            modifier = modifier
                .wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(genres.size) { index ->
                val genre = genres[index]
                Chip(
                    title = genre.name,
                    isSelected = selectedGenreId == genre.id,
                    onClick = { onGenreSelected(genre) }
                )
            }
        }
    }
}