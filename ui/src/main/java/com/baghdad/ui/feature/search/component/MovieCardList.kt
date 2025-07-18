package com.baghdad.ui.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.design_system.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun MovieCardList(
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    onSavedClick: (Long) -> Unit,
    onMovieClick: (id: Long, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyPagingVerticalGrid<SearchScreenState.MovieUiState>(
        items = movies,
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 150.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        key = { it.id }
    ) { movie ->
        HomeCard(
            url = movie.posterPictureURL,
            contentDescription = null,
            isSaved = movie.isSaved,
            onSavedClick = { onSavedClick(movie.id) },
            onClick = {
                onMovieClick(
                    movie.id,
                    movie.posterPictureURL
                )
            },
            modifier = Modifier.aspectRatio(0.8f)
        )
    }
}