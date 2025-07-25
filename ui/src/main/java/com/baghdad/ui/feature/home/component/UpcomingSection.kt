package com.baghdad.ui.feature.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.design_system.modifier.shimmerEffect
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.DefaultErrorItem
import com.baghdad.ui.feature.component.lazyPaging.DefaultLoadingItem
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState

fun LazyGridScope.upcomingSection(
    selectedGenreId: Long?,
    genres: List<GenreUiState>,
    isGenresLoading: Boolean,
    onGenreSelected: (GenreUiState?) -> Unit,
    upcomingItems: LazyPagingItems<UpcomingItemUiState>,
    isUpcomingItemsLoading: Boolean,
    onUpcomingItemClicked: (UpcomingItemUiState) -> Unit,
    onUpcomingItemSaveClicked: (UpcomingItemUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isUpcomingItemsLoading.not() && isGenresLoading.not() && upcomingItems.itemCount == 0) return
    item(span = { GridItemSpan(maxLineSpan) }) {
        UpcomingSectionHeader(
            modifier = modifier,
            genres = genres,
            isGenresLoading = isGenresLoading,
            onSelectGenre = onGenreSelected,
            selectedGenreId = selectedGenreId,
        )
    }
    if (isUpcomingItemsLoading) {
        items(20) { index ->
            val itemsPerRow = maxOf(1, (LocalConfiguration.current.screenWidthDp / 158))
            val isFirstInRow = index % itemsPerRow == 0
            val isLastInRow = (index + 1) % itemsPerRow == 0 || index == upcomingItems.itemCount - 1

            Box(
                modifier =
                    Modifier
                        .size(width = 158.dp, height = 210.dp)
                        .padding(
                            top = 12.dp,
                            start = if (isFirstInRow) 16.dp else 0.dp,
                            end = if (isLastInRow) 16.dp else 0.dp,
                        )
                        .background(Theme.color.surface, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .shimmerEffect(),
            )
        }
    } else {
        items(count = upcomingItems.itemCount, key = upcomingItems.itemKey { it.id }) { index ->
            val item = upcomingItems[index] ?: return@items
            val itemsPerRow = maxOf(1, (LocalConfiguration.current.screenWidthDp / 158))
            val isFirstInRow = index % itemsPerRow == 0
            val isLastInRow = (index + 1) % itemsPerRow == 0 || index == upcomingItems.itemCount - 1
            HomeCard(
                url = item.imageUrl,
                contentDescription = null,
                isSaved = item.isSaved,
                onSavedClick = { onUpcomingItemSaveClicked(item) },
                onClick = {
                    onUpcomingItemClicked(item)
                },
                modifier =
                    Modifier
                        .aspectRatio(0.8f)
                        .padding(
                            top = 12.dp,
                            start = if (isFirstInRow) 16.dp else 0.dp,
                            end = if (isLastInRow) 16.dp else 0.dp,
                        ),
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
}

@Composable
private fun UpcomingSectionHeader(
    selectedGenreId: Long?,
    genres: List<GenreUiState>,
    isGenresLoading: Boolean,
    onSelectGenre: (GenreUiState?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(modifier = modifier, targetState = isGenresLoading) { isLoading ->
        if (isLoading) {
            UpcomingSectionHeaderLoadingPlaceHolder()
        } else {
            Column {
                SectionHeader(
                    title = stringResource(R.string.upcoming),
                    isShowAllVisible = false,
                    modifier = modifier.wrapContentSize(),
                )
                LazyRow(
                    modifier =
                        Modifier
                            .wrapContentSize()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    item {
                        Chip(
                            title = stringResource(com.baghdad.design_system.R.string.all),
                            isSelected = selectedGenreId == null,
                            onClick = { onSelectGenre(null) },
                        )
                    }
                    items(genres.size) { index ->
                        val genre = genres[index]
                        Chip(
                            title = genre.name,
                            isSelected = selectedGenreId == genre.id,
                            onClick = { onSelectGenre(genre) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpcomingSectionHeaderLoadingPlaceHolder(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .size(width = 166.dp, height = 30.dp)
                    .padding(horizontal = 16.dp)
                    .background(Theme.color.surface, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
        )
        LazyRow(
            modifier =
                Modifier
                    .wrapContentSize()
                .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(20) {
                Box(
                    modifier =
                        Modifier
                            .size(width = 64.dp, height = 38.dp)
                            .background(Theme.color.surface, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect(),
                )
            }
        }
    }
}
