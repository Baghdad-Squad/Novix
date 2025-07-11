package com.baghdad.feature.search.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BaseRangeSlider
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.viewmodel.search.SearchInteractionListener
import com.baghdad.viewmodel.search.SearchScreenState

@Composable
fun ReleasedYearSlider(
    listener: SearchInteractionListener,
    uiState: SearchScreenState.FilterBottomSheetUiState,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.released_year),
            style = Theme.typography.title.small,
            color = Theme.color.title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MinMaxReleaseYear(uiState = uiState)

        BaseRangeSlider(
            value = uiState.selectedRange,
            onValueChange = { newRange ->
                listener.onYearRangeSelected(newRange)
            },
            valueRange = uiState.valueRange
        )
    }
}

@Composable
fun MinMaxReleaseYear(
    uiState: SearchScreenState.FilterBottomSheetUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = uiState.selectedRange.start.toInt().toString(),
            color = Theme.color.body,
            style = Theme.typography.label.small,
        )
        Text(
            text = uiState.selectedRange.endInclusive.toInt().toString(),
            color = Theme.color.body,
            style = Theme.typography.label.small,
        )
    }
}