package com.baghdad.ui.feature.categoryTvShows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.R
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.feature.util.rememberSaveableLazyGridState
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsEffect
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsInteractionListener
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsState
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage


@Composable
fun CategoryTvShowsScreen(
    viewModel: CategoryTvShowsViewModel = hiltViewModel(),
    handleNavigation: (CategoriesNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    CategoryTvShowsContent(
        uiState = uiState, listener = viewModel, snackBarState = snackBarState
    )
}

private fun handleEffect(
    effect: CategoryTvShowsEffect, handleNavigation: (CategoriesNavEvent) -> Unit
) {
    when (effect) {
        is CategoryTvShowsEffect.NavigateBack -> handleNavigation(
            CategoriesNavEvent.NavigateBack
        )

        is CategoryTvShowsEffect.NavigateToTvShowDetails -> handleNavigation(
            CategoriesNavEvent.NavigateToTvShowDetails(effect.tvShowId)
        )
    }
}

@Composable
private fun CategoryTvShowsContent(
    uiState: CategoryTvShowsState,
    listener: CategoryTvShowsInteractionListener,
    snackBarState: SnackBarState,
) {
    val lazyPagingTvShows = uiState.tvShowsFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(R.drawable.ic_go_back),
                    onClick = { listener.onBackClicked() },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 12.dp,
                    )
                )
                Text(
                    text = uiState.categoryName,
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }, snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
            )
        },
        isLoading = uiState.isLoading,
        backgroundBlur = {
            BackgroundBlur(modifier = Modifier.zIndex(999f))
        }) {
        Column {
            LazyPagingVerticalGrid(
                items = lazyPagingTvShows,
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberSaveableLazyGridState()
            ) { tvShow ->
                HomeCard(
                    url = tvShow.posterPictureURL,
                    contentDescription = null,
                    onClick = { listener.onTvShowClicked(tvShow.id) },
                    isSaveToListVisible = false,
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }

        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}