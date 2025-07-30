package com.baghdad.ui.feature.tvShowDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R

@Composable
fun SeasonSection(
    modifier: Modifier = Modifier,
    seasonCount: Int,
    selectedSeasonIndex: Int,
    onSeasonSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.season),
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp)
        )
        SeasonTabs(
            seasonCount = seasonCount,
            selectedSeasonIndex = selectedSeasonIndex,
            onSeasonSelected = onSeasonSelected
        )
    }
}

@Composable
fun SeasonTabs(
    seasonCount: Int,
    selectedSeasonIndex: Int,
    onSeasonSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(seasonCount) {
            Chip(
                title = stringResource(R.string.season_number_template, it + 1),
                isSelected = selectedSeasonIndex == it,
                onClick = { onSeasonSelected(it) }
            )
        }
    }
}