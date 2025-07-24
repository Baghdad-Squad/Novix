package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.HorizontalCarousel
import com.baghdad.design_system.component.SectionHeader
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SectionHeader(
            title = stringResource(R.string.continue_watching),
            isShowAllVisible = true,
            onClick = onViewAllClick,
        )

        HorizontalCarousel(
            items = items,
        ) { item ->
            HomeCard(
                url = item.imageUrl,
                contentDescription = null,
                isSaved = item.isSaved,
                onSavedClick = { onSaveClick(item) },
                onClick = { onClick(item) },
                modifier = Modifier.aspectRatio(0.8f)
            )
        }
    }
}