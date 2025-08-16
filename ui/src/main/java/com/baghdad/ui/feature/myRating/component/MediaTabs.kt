package com.baghdad.ui.feature.myRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.baghdad.design_system.component.Chip
import com.baghdad.ui.R
import com.baghdad.viewmodel.myRating.MyRatingState

@Composable
fun MediaTabs(
    selectedTab: MyRatingState.MediaTab?,
    onTabClick: (MyRatingState.MediaTab?) -> Unit,
    genresScrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.wrapContentSize(),
        state = genresScrollState,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Chip(
                title = stringResource(R.string.all),
                isSelected = selectedTab == null,
                onClick = { onTabClick(null) }
            )
        }
        item {
            Chip(
                title = stringResource(com.baghdad.ui.R.string.tab_movies),
                isSelected = selectedTab == MyRatingState.MediaTab.MOVIE,
                onClick = { onTabClick(MyRatingState.MediaTab.MOVIE) }
            )
        }
        item {
            Chip(
                title = stringResource(com.baghdad.ui.R.string.tab_tv_shows),
                isSelected = selectedTab == MyRatingState.MediaTab.TV_SHOW,
                onClick = { onTabClick(MyRatingState.MediaTab.TV_SHOW) }
            )
        }
    }
}