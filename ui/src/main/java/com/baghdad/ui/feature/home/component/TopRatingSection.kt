package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.viewmodel.home.HomeScreenState

@Composable
fun TopRatingSection(
    isLoading: Boolean,
    items: List<HomeScreenState.TopRatingItemUiState>,
    onClick: (HomeScreenState.TopRatingItemUiState) -> Unit,
    onSaveClick: (HomeScreenState.TopRatingItemUiState) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    HomeHorizontalCarouselSection(
        title = stringResource(R.string.top_rating),
        isLoading = isLoading,
        items = items,
        onViewAllClick = onViewAllClick,
        modifier = modifier,
    ) { item, itemModifier ->
        HomeCard(
            url = item.imageUrl,
            contentDescription = stringResource(R.string.top_rating_item),
            isSaved = item.isSaved,
            onSavedClick = { onSaveClick(item) },
            onClick = { onClick(item) },
            modifier = itemModifier.aspectRatio(0.8f)
        )
    }
}