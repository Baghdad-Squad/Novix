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

@Composable
fun ReleasedYearSlider(
    minimumYear: Int,
    maximumYear: Int,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
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

        MinMaxReleaseYear(
            minimumYear = minimumYear,
            maximumYear = maximumYear,
        )

        BaseRangeSlider(
            value = minimumYear.toFloat()..maximumYear.toFloat(),
            onValueChange = onValueChange,
            valueRange = 1990f..2025f,
        )
    }
}

@Composable
fun MinMaxReleaseYear(
    minimumYear: Int,
    maximumYear: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = minimumYear.toString(),
            color = Theme.color.body,
            style = Theme.typography.label.small,
        )
        Text(
            text = maximumYear.toString(),
            color = Theme.color.body,
            style = Theme.typography.label.small,
        )
    }
}