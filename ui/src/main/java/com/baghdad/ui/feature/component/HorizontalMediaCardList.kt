package com.baghdad.ui.feature.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> HorizontalMediaCardList(
    items: List<T>,
    imageUrl: (T) -> String,
    onSavedClick: (T) -> Unit,
    onCardClick: (T) -> Unit,
    isSaved: (T) -> Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = contentPadding,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        this.items(
            items = items,
        ) { item ->
            HomeCard(
                url = imageUrl(item),
                contentDescription = "item",
                isSaved = isSaved(item),
                onSavedClick = { onSavedClick(item) },
                onClick = { onCardClick(item) },
            )
        }
    }
}