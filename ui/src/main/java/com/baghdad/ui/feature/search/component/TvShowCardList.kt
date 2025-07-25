package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun TvShowCardList(
    tvShows: LazyPagingItems<SearchScreenState.TvShowUiState>,
    state: LazyGridState,
    onSavedClick: (Long) -> Unit,
    onTVShowClick: (id: Long, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyPagingVerticalGrid<SearchScreenState.TvShowUiState>(
        items = tvShows,
        state = state,
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 150.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        key = { it.id },
        contentPadding = PaddingValues(bottom = 8.dp)
    ) { tvShow ->
        HomeCard(
            url = tvShow.posterPictureURL,
            contentDescription = null,
            isSaved = tvShow.isSaved,
            onSavedClick = { onSavedClick(tvShow.id) },
            onClick = {
                onTVShowClick(
                    tvShow.id,
                    tvShow.posterPictureURL
                )
            },
            modifier = Modifier.aspectRatio(0.8f)
        )
    }
}