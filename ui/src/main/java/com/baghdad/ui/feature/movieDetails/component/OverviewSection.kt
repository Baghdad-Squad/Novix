package com.baghdad.ui.feature.movieDetails.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.ExpandableText

@Composable
fun OverviewSection(
    overview: String,
    isExtended: Boolean,
    onExtendClicked: () -> Unit,
) {
    Column(modifier = Modifier.offset(y = (-48).dp)) {
        TextSection(
            text = stringResource(com.baghdad.ui.R.string.overview),
            modifier = Modifier.padding(

                end = 16.dp,
            ),

            )
        ExpandableText(
            modifier = Modifier.padding(horizontal = 16.dp),
            isExpanded = isExtended,
            text = overview
        ) {
            onExtendClicked()
        }
    }

}