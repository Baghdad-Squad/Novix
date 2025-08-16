package com.baghdad.ui.feature.movieDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baghdad.design_system.component.ExpandableText
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme

@Composable
fun OverviewSection(
    overview: String,
    isExtended: Boolean,
    modifier: Modifier = Modifier,
    readMoreMaxLines: Int = 4,
    onExtendClicked: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(com.baghdad.ui.R.string.overview),
            fontSize = 18.sp,
            style = Theme.typography.title.medium,
            color = Theme.color.title,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExpandableText(
            isExpanded = isExtended,
            text = overview,
            readMoreMaxLines = readMoreMaxLines,
            onExpandedChange = { onExtendClicked() }
        )
    }

}