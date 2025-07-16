package com.baghdad.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.baghdad.design_system.component.HomeCard
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun MovieCardList(
    movies: LazyPagingItems<SearchScreenState.MovieUiState>,
    onSavedClick: (Long) -> Unit,
    onMovieClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        items(movies.itemCount) { index ->
            val movie = movies[index] ?: return@items
            HomeCard(
                url = movie.posterPictureURL,
                contentDescription = null,
                isSaved = movie.isSaved,
                onSavedClick = { onSavedClick(movie.id) },
                onClick = {
                    onMovieClick(
                        movie.id,
                    )
                },
                modifier = Modifier.aspectRatio(0.8f)
            )
        }
    }
}