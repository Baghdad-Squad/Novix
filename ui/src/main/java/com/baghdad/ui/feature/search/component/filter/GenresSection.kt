package com.baghdad.ui.feature.search.component.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Icon
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.OutlinedButton
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.search.SearchScreenState.GenreUiState

@Composable
fun GenresSection(
    allGenres: List<GenreUiState>,
    selectedGenres: List<GenreUiState>,
    isGenresError: Boolean,
    onReloadGenres: () -> Unit,
    onGenreSelected: (GenreUiState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedGenreNames = selectedGenres.map { it.name }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.genres),
            style = Theme.typography.title.small,
            color = Theme.color.title,
        )
        AnimatedContent(isGenresError) { isError ->
            if (isError) {
                ErrorContent { onReloadGenres() }
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    allGenres.forEach { genre ->
                        Chip(
                            title = genre.name,
                            isSelected = selectedGenreNames.contains(genre.name),
                            onClick = { onGenreSelected(genre) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_alert),
            tint = Theme.color.redAccent,
            contentDescription = stringResource(com.baghdad.ui.R.string.an_error_occurred),
            modifier = Modifier.size(56.dp),
        )
        Text(
            text = stringResource(com.baghdad.ui.R.string.an_error_occurred_while_loading_genres),
            color = Theme.color.title,
            style = Theme.typography.body.medium,
        )
        OutlinedButton(
            label = stringResource(com.baghdad.ui.R.string.retry),
            onClick = onRetry,
        )
    }
}

@Preview
@Composable
private fun ErrorContentPreview() {
    NovixTheme {
        ErrorContent(onRetry = {})
    }
}
