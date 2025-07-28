package com.baghdad.ui.feature.trendingTvShow.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowScreenState.GenreUiState

@Composable
fun GenresSection(
    allGenres: List<GenreUiState>,
    selectedGenre: Long?,
    onGenreSelected: (GenreUiState?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        item {
            Chip(
                title = stringResource(com.baghdad.design_system.R.string.all),
                isSelected = selectedGenre == null,
                onClick = { onGenreSelected(null) },
            )
        }
        items(allGenres.size) { index ->
            val genre = allGenres[index]
            Chip(
                title = genre.name,
                isSelected = selectedGenre == genre.id,
                onClick = { onGenreSelected(genre) }
            )
        }
    }
}