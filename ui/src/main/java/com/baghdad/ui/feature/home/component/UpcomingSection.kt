package com.baghdad.ui.feature.home.component

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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.SectionHeader
import com.baghdad.design_system.modifier.shimmerEffect
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.viewmodel.home.HomeScreenState.GenreUiState
import com.baghdad.viewmodel.home.HomeScreenState.UpcomingItemUiState

fun LazyGridScope.upcomingSection(
    selectedGenreId: Long?,
    genres: List<GenreUiState>,
    onGenreSelected: (GenreUiState?) -> Unit,
    upcomingItems: List<UpcomingItemUiState>,
    onUpcomingItemClicked: (UpcomingItemUiState) -> Unit,
    onUpcomingItemSaveClicked: (UpcomingItemUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (upcomingItems.isEmpty()) return
    upcomingSectionHeader(
        modifier = modifier,
        genres = genres,
        onSelectGenre = onGenreSelected,
        selectedGenreId = selectedGenreId,
    )

    itemsIndexed(
        items = upcomingItems,
        key = { index, _ -> index },
    ) { index, item ->
        val itemsPerRow = maxOf(1, (LocalConfiguration.current.screenWidthDp / 150))
        val isInFirstRow = index < itemsPerRow
        val isFirstInRow = index % itemsPerRow == 0
        val isLastInRow = (index + 1) % itemsPerRow == 0 || index == upcomingItems.size - 1
        HomeCard(
            url = item.imageUrl,
            contentDescription = null,
            isSaved = item.isSaved,
            isLoadingEnabled = false,
            onSavedClick = { onUpcomingItemSaveClicked(item) },
            onClick = {
                onUpcomingItemClicked(item)
            },
            modifier =
                Modifier
                    .aspectRatio(0.8f)
                    .padding(
                        top = if (isInFirstRow) 4.dp else 12.dp,
                        start = if (isFirstInRow) 16.dp else 0.dp,
                        end = if (isLastInRow) 16.dp else 0.dp,
                    ),
        )
    }
}

private fun LazyGridScope.upcomingSectionHeader(
    selectedGenreId: Long?,
    genres: List<GenreUiState>,
    onSelectGenre: (GenreUiState?) -> Unit,
    modifier: Modifier = Modifier,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        SectionHeader(
            title = stringResource(R.string.upcoming),
            isShowAllVisible = false,
            modifier =
                modifier
                    .wrapContentSize()
                    .padding(bottom = 8.dp),
        )
    }
    stickyHeader {
        LazyRow(
            modifier =
                Modifier
                    .wrapContentSize()
                    .background(Theme.color.surface)
                    .padding(top = 4.dp, bottom = 8.dp),
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

fun LazyGridScope.upcomingSectionLoading(
    isLoading: Boolean,
    selectedGenreId: Long?,
    genres: List<GenreUiState>,
    onGenreSelected: (GenreUiState?) -> Unit,
    upcomingItems: List<UpcomingItemUiState>,
    onUpcomingItemClicked: (UpcomingItemUiState) -> Unit,
    onUpcomingItemSaveClicked: (UpcomingItemUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (upcomingItems.isEmpty() && !isLoading) return

    upcomingSectionHeader(
        modifier = modifier,
        genres = genres,
        onSelectGenre = onGenreSelected,
        selectedGenreId = selectedGenreId,
    )

    if (isLoading) {
        items(
            count = 20,
            key = { it },
        ) { index ->
            val itemsPerRow = maxOf(1, (LocalConfiguration.current.screenWidthDp / 150))
            val isInFirstRow = index < itemsPerRow
            val isFirstInRow = index % itemsPerRow == 0
            val isLastInRow = (index + 1) % itemsPerRow == 0 || index == 19

            Box(
                modifier = Modifier
                    .aspectRatio(0.8f)
                    .padding(
                        top = if (isInFirstRow) 4.dp else 12.dp,
                        start = if (isFirstInRow) 16.dp else 0.dp,
                        end = if (isLastInRow) 16.dp else 0.dp,
                    )
                    .background(Theme.color.surface, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect(),
            )
        }
    } else {
        itemsIndexed(
            items = upcomingItems,
            key = { index, _ -> index },
        ) { index, item ->
            val itemsPerRow = maxOf(1, (LocalConfiguration.current.screenWidthDp / 150))
            val isInFirstRow = index < itemsPerRow
            val isFirstInRow = index % itemsPerRow == 0
            val isLastInRow = (index + 1) % itemsPerRow == 0 || index == upcomingItems.size - 1

            HomeCard(
                url = item.imageUrl,
                contentDescription = null,
                isSaved = item.isSaved,
                onSavedClick = { onUpcomingItemSaveClicked(item) },
                onClick = { onUpcomingItemClicked(item) },
                modifier = Modifier
                    .aspectRatio(0.8f)
                    .padding(
                        top = if (isInFirstRow) 4.dp else 12.dp,
                        start = if (isFirstInRow) 16.dp else 0.dp,
                        end = if (isLastInRow) 16.dp else 0.dp,
                    ),
            )
        }
    }
}
