package com.baghdad.ui.feature.tvShowDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.ExpandableText
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R


@Composable
fun TvShowOverviewSection(
    overview: String,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit,
    modifier: Modifier = Modifier,
    readMoreMaxLines: Int = 4,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(R.string.overview),
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExpandableText(
            isExpanded = isExpanded,
            text = overview,
            readMoreMaxLines = readMoreMaxLines
        ) {
            onExpandedChange()
        }
    }
}
