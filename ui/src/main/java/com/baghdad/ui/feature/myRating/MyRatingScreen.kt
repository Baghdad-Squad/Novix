package com.baghdad.ui.feature.myRating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.navigation.graph.myAccount.MyAccountNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.myRating.MyRatingEffect
import com.baghdad.viewmodel.myRating.MyRatingInteractionListener
import com.baghdad.viewmodel.myRating.MyRatingState
import com.baghdad.viewmodel.myRating.MyRatingViewModel

@Composable
fun MyRatingScreen(
    viewModel: MyRatingViewModel = hiltViewModel(),
    handleNavigation: (MyAccountNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mediaItem = uiState.mediaFlow.collectAsLazyPagingItems()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is MyRatingEffect.NavigateBack -> handleNavigation(MyAccountNavEvent.NavigateBack)
            is MyRatingEffect.NavigateToMovieDetails -> handleNavigation(
                MyAccountNavEvent.NavigateToMovieDetails(
                    effect.movieId
                )
            )

            is MyRatingEffect.NavigateToTvShowDetails -> handleNavigation(
                MyAccountNavEvent.NavigateToTvShowDetails(
                    effect.tvShowId
                )
            )
        }
    }

    MyRatingContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
        mediaItems = mediaItem,
    )
}

@Composable
private fun MyRatingContent(
    uiState: MyRatingState,
    listener: MyRatingInteractionListener,
    snackBarState: SnackBarState,
    mediaItems: LazyPagingItems<MyRatingState.MediaItemUiState>
) {
    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),

        isLoading = uiState.isLoading,

        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,

        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
                position = position
            )
        },
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 22.dp, bottom = 8.dp),
                    onGoBackClick = {
                        listener.onBackClick()
                    },
                    screenTitle = stringResource(com.baghdad.ui.R.string.my_rating),
                )
                MediaTabs(
                    selectedTab = uiState.selectedMediaTab,
                    onTabClick = { listener.onMediaTabClick(it) },
                    genresScrollState = rememberLazyListState(),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        },

        backgroundBlur = { BackgroundBlur() }
    ) {
        LazyPagingVerticalGrid<MyRatingState.MediaItemUiState>(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            items = mediaItems,
        ) { media ->
            RatingCard(
                url = media.posterPictureURL,
                rating = media.rating,
                contentDescription = null,
                onClick = { listener.onMediaClick(media.id, media.contentType) },
                onDeleteClick = { listener.onDeleteClick(media.id, media.contentType) }
            )
        }
    }
}


private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}

@Composable
private fun MediaTabs(
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
