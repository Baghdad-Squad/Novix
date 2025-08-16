package com.baghdad.ui.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.viewmodel.home.HomeScreenState.PopularItemUiState

@Composable
fun PopularSection(
    isLoading: Boolean,
    popularItems: List<PopularItemUiState>,
    onClick: (PopularItemUiState) -> Unit,
    onSaveClick: (PopularItemUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.popular),
            style = Theme.typography.headline.small,
            color = Theme.color.title,
            modifier = Modifier.padding(start = 16.dp)
        )
        PopularCardPager(
            isLoading = isLoading,
            items = popularItems,
            onClick = onClick,
            onSaveClick = onSaveClick,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}