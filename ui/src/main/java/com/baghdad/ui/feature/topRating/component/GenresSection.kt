package com.baghdad.ui.feature.topRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.viewmodel.topRating.TopRatingMovieState.GenreUiState

@Composable
fun GenresSection(
    allGenres: List<GenreUiState>,
    selectedGenres: GenreUiState,
    onGenreSelected: (GenreUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedGenreNames = selectedGenres
        .let { if (it.name.isNotEmpty()) listOf(it.name) else emptyList() }

    LazyRow(
        modifier = modifier
            .wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(allGenres.size) { index ->
            val genre = allGenres[index]
            Chip(
                title = genre.name,
                isSelected = selectedGenreNames.contains(genre.name),
                onClick = { onGenreSelected(genre) }
            )
        }
    }
}
