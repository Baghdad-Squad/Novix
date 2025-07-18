package com.baghdad.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HomeCard
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun TvShowCardList(
    tvShows: List<SearchScreenState.TvShowUiState>,
    onSavedClick: (Long) -> Unit,
    onTVShowClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 8.dp)

    ) {
        items(tvShows) { tvShow ->
            HomeCard(
                url = tvShow.posterPictureURL,
                contentDescription = null,
                isSaved = tvShow.isSaved,
                onSavedClick = { onSavedClick(tvShow.id) },
                onClick = {
                    onTVShowClick(
                        tvShow.id,
                    )
                },
                modifier = Modifier.aspectRatio(0.8f)
            )
        }
    }
}