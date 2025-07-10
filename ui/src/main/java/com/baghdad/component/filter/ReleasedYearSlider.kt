package com.baghdad.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BaseRangeSlider
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun ReleasedYearSlider(modifier: Modifier = Modifier) {
    var sliderValue by remember { mutableStateOf(1995f..2012f) }
    val valueRange = 1990f..2025f

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

        MinMaxReleaseYear()

        BaseRangeSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = valueRange
        )
    }
}

@Composable
fun MinMaxReleaseYear(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "1990", /*TODO*/
            color = Theme.color.body,
            style = Theme.typography.label.small,
        )
        Text(
            text = "2025", /*TODO*/
            color = Theme.color.body,
            style = Theme.typography.label.small,
        )
    }
}