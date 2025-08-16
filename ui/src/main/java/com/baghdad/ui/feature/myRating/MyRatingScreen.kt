package com.baghdad.ui.feature.myRating

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
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
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.EmptyListScreen
import com.baghdad.ui.feature.myRating.component.MediaTabs
import com.baghdad.ui.feature.myRating.component.MyRatingVerticalGrid
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
        snackBarState = snackBarState
    )
}

@Composable
private fun MyRatingContent(
    uiState: MyRatingState,
    listener: MyRatingInteractionListener,
    snackBarState: SnackBarState,
) {
    val mediaItems = uiState.mediaFlow.collectAsLazyPagingItems()
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
                    onGoBackClick = { listener.onBackClick() },
                    screenTitle = stringResource(com.baghdad.ui.R.string.my_rating),
                )
                AnimatedVisibility(
                    mediaItems.itemCount != 0 || uiState.isLoading,
                ) {
                    MediaTabs(
                        selectedTab = uiState.selectedMediaTab,
                        onTabClick = { listener.onMediaTabClick(it) },
                        genresScrollState = rememberLazyListState(),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        },

        backgroundBlur = { BackgroundBlur() }
    ) {

        AnimatedContent(
            targetState = mediaItems.itemCount == 0 && uiState.isLoading.not(),
        ) { emptyRating ->
            if (emptyRating) {
                EmptyListScreen()
            } else {
                MyRatingVerticalGrid(
                    mediaItems = mediaItems,
                    onMediaClick = listener::onMediaClick,
                    onDeleteClick = listener::onDeleteClick
                )
            }
        }
    }
}


private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
