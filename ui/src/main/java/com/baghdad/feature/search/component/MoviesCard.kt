package com.baghdad.feature.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HomeCard

@Composable
fun MoviesCard(
    url: String,
    contentDescription: String,
    isSaved: Boolean,
    onSavedClick: () -> Unit,
    modifier: Modifier
) {
    LazyRow(
        modifier = Modifier
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) {
            HomeCard(
                url = url,
                contentDescription = contentDescription,
                isSaved = isSaved,
                onSavedClick = { onSavedClick() },
                modifier = modifier
            )
        }
    }
}