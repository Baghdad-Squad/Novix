package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.baghdad.ui.R
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.viewmodel.home.HomeScreenState

@Composable
fun ContinueWatchingSection(
    isLoading: Boolean,
    items: List<HomeScreenState.ContinueWatchingItemUiState>,
    onClick: (HomeScreenState.ContinueWatchingItemUiState) -> Unit,
    onSaveClick: (HomeScreenState.ContinueWatchingItemUiState) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeHorizontalCarouselSection(
        title = stringResource(R.string.continue_watching),
        isLoading = isLoading,
        items = items,
        onViewAllClick = onViewAllClick,
        modifier = modifier,
    ) { item, showSaveIcon ->
        HomeCard(
            url = item.imageUrl,
            contentDescription = stringResource(R.string.continue_watching_item),
            isSaved = item.isSaved,
            onSavedClick = { onSaveClick(item) },
            onClick = { onClick(item) },
            modifier = Modifier.aspectRatio(0.8f),
            isSaveToListVisible = showSaveIcon && item.contentType == HomeScreenState.ContinueWatchingItemUiState.ContentType.MOVIE,
        )
    }
}
