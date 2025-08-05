package com.baghdad.ui.feature.savedListDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.ui.R
import com.baghdad.viewmodel.savedListDetails.SavedListTab

@Composable
fun MediaCategoriesSection(
    mediaCategories: List<SavedListTab>,
    selectedCategory: SavedListTab,
    onCategorySelected: (SavedListTab) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(mediaCategories.size) { index ->
            val category = mediaCategories[index]

            val displayTitle = when (category) {
                SavedListTab.ALL -> stringResource(R.string.all)
                SavedListTab.MOVIES -> stringResource(R.string.movies)
                SavedListTab.TV_SHOWS -> stringResource(R.string.tv_shows)
            }

            Chip(
                title = displayTitle,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}
