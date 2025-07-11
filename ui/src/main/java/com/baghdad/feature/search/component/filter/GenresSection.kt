package com.baghdad.feature.search.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun GenresSection(
    listener: SearchInteractionListener,
    uiState: SearchScreenState.FilterBottomSheetUiState,
    modifier: Modifier = Modifier
) {
    val genres = when (uiState.isFilterMovie) {
        true -> uiState.movieGenres
        false -> uiState.tvShowGenres
    }
    val selectedGenres = uiState.selectedGenres.map { it.name }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.genres),
            style = Theme.typography.title.small,
            color = Theme.color.title,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                Chip(
                    title = genre.name,
                    isSelected = selectedGenres.contains(genre.name),
                    onClick = {
                        listener.onGenresSelected(genre)
                    }
                )
            }
        }
    }
}