package com.baghdad.ui.feature.topMoviePicks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HomeCard
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksInteractionListener
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksState


@Composable
private fun TopMoviePicksContent(
    uiState: TopMoviePicksState,
    listener: TopMoviePicksInteractionListener,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
        items(uiState.movies) { movie ->
            HomeCard(
                url = movie.posterPictureURL,
                contentDescription = null,
                isSaved = movie.isSaved,
                onSavedClick = { listener.onSaveMovieClicked(movie.id) },
                onClick = { listener.onMovieDetailsClicked(movie.id) },
                modifier = Modifier.aspectRatio(0.8f)
            )
        }
    }
}