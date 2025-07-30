package com.baghdad.ui.feature.topRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.viewmodel.topRating.TopRatingState.GenreUiState

@Composable
fun GenresSection(
    allGenres: List<GenreUiState>,
    selectedGenres: Long?,
    onGenreSelected: (GenreUiState?) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .wrapContentSize(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        item {
            Chip(
                title = stringResource(com.baghdad.design_system.R.string.all),
                isSelected = selectedGenres == null,
                onClick = { onGenreSelected(null) },
            )
        }
        items(allGenres.size) { index ->
            val genre = allGenres[index]
            Chip(
                title = genre.name,
                isSelected = selectedGenres == genre.id,
                onClick = { onGenreSelected(genre) }
            )
        }
    }
}
