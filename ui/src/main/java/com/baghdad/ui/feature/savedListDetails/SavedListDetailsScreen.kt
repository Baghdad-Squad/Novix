package com.baghdad.ui.feature.savedListDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.component.scaffold.Scaffold
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.EmptyListScreen
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.feature.savedListDetails.component.ConfirmListDeletionBottomSheet
import com.baghdad.ui.navigation.graph.myLists.MyListsNavEvent
import com.baghdad.ui.util.toScaffoldSnackBarState
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsEffect
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsInteractionListener
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsScreenState
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsViewModel

@Composable
fun SavedListDetailsScreen(
    viewModel: SavedListDetailsViewModel = hiltViewModel(),
    handleNavigation: (MyListsNavEvent) -> Unit = {}
) {
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    SavedListDetailsContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

private fun handleEffect(
    effect: SavedListDetailsEffect,
    handleNavigation: (MyListsNavEvent) -> Unit
) {
    when (effect) {
        is SavedListDetailsEffect.NavigateBack ->
            handleNavigation(MyListsNavEvent.NavigateToMyLists)

        is SavedListDetailsEffect.NavigateToMovieDetails ->
            handleNavigation(MyListsNavEvent.NavigateToMovieDetails(effect.movieId))

    }
}

@Composable
fun SavedListDetailsContent(
    uiState: SavedListDetailsScreenState,
    listener: SavedListDetailsInteractionListener,
    snackBarState: SnackBarState,
) {
    val mediaItems = uiState.mediaFlow.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.background(Theme.color.surface),
        backgroundContent = { BackgroundBlur() },
        isLoading = uiState.isLoading,
        topBar = {
            SavedListDetailTopBar(
                uiState = uiState,
                listener = listener
            )
        },
        snackBarState = snackBarState.toScaffoldSnackBarState(::mapSnackBarMessage),
        onSnackBarActionClick = listener::onSnackBarActionLabelClick,
    ) {

        AnimatedContent(
            targetState = mediaItems.itemCount == 0 && uiState.isLoading.not(),
        ) { isEmptyList ->
            if (isEmptyList) {
                EmptyListScreen()
            } else {
                ListContent(listener, mediaItems)
            }
        }
    }
    ConfirmListDeletionBottomSheet(
        onBottomSheetCloseClick = { listener.onDeleteListBottomSheetDismiss() },
        title = stringResource(R.string.deleted_list),
        description = stringResource(R.string.delete_description),
        isVisible = uiState.isConfirmDeleteDialogVisible,
        onDeleteClick = listener::onDeleteListBottomSheetDeleteClick
    )
}

@Composable
private fun SavedListDetailTopBar(
    uiState: SavedListDetailsScreenState,
    listener: SavedListDetailsInteractionListener
) {
    Column {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 22.dp, bottom = 8.dp),
            onGoBackClick = listener::onBackClick,
            screenTitle = uiState.savedList.name,
            maxLines = 1,
            textEllipsize = TextOverflow.Ellipsis
        ) {
            IconButton(
                icon = painterResource(com.baghdad.design_system.R.drawable.ic_delete),
                tintIcon = Theme.color.redAccent,
                onClick = listener::onDeleteClick,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

    }
}

@Composable
private fun ListContent(
    listener: SavedListDetailsInteractionListener,
    mediaItems: LazyPagingItems<SavedListDetailsScreenState.SavedListDetailsMovieUiState>
) {
    LazyPagingVerticalGrid(
        items = mediaItems,
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 12.dp,
            top = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) { movie ->
        HomeCard(
            url = movie.posterUrl,
            isSaved = true,
            contentDescription = stringResource(R.string.movie_card),
            onSavedClick = { listener.onRemoveSavedMovieClick(movie.id) },
            onClick = { listener.onMovieClick(movie.id) }
        )
    }
}

private fun mapSnackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()