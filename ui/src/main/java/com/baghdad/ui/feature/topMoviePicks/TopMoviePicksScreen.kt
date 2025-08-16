package com.baghdad.ui.feature.topMoviePicks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.bottomSheet.AddListBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.SavedListBottomSheet
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToLogin
import com.baghdad.ui.navigation.graph.actorDetails.ActorDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksEffect
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksInteractionListener
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksState
import com.baghdad.viewmodel.topMoviePicks.TopMoviePicksViewModel

@Composable
fun TopMoviePicksScreen(
    viewModel: TopMoviePicksViewModel = hiltViewModel(),
    handleNavigation: (ActorDetailsNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    TopMoviePicksContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
    )
}

private fun handleEffect(
    effect: TopMoviePicksEffect,
    handleNavigation: (ActorDetailsNavEvent) -> Unit,
) {
    when (effect) {
        is TopMoviePicksEffect.NavigateBack ->
            handleNavigation(
                ActorDetailsNavEvent.NavigateBack,
            )

        is TopMoviePicksEffect.NavigateToMovieDetails ->
            handleNavigation(NavigateToMovieDetails(effect.movieId))

        is TopMoviePicksEffect.NavigateToLogin ->
            handleNavigation(NavigateToLogin)
    }
}

@Composable
private fun TopMoviePicksContent(
    uiState: TopMoviePicksState,
    listener: TopMoviePicksInteractionListener,
    snackBarState: SnackBarState
) {
    val savedLists = uiState.addToListBottomSheetState.savedLists.collectAsLazyPagingItems()

    Scaffold(
        modifier =
            Modifier
                .background(Theme.color.surface)
                .systemBarsPadding()
                .statusBarsPadding(),

        isLoading = uiState.isLoading,

        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClicked,
                isVisible = snackBarState.isVisible,
                position = position,
            )
        },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,

        topBar = {
            TopAppBar(
                onGoBackClick = listener::onBackClicked,
                screenTitle = stringResource(com.baghdad.ui.R.string.top_movies_picks),
                modifier =
                    Modifier
                        .padding(vertical = 8.dp)
                        .padding(top = 12.dp),
            )
        },

        backgroundBlur = { BackgroundBlur() }

    ) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 8.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.movies) { movie ->
                HomeCard(
                    url = movie.posterPictureURL,
                    contentDescription = null,
                    isSaved = movie.isSaved,
                    onSavedClick = { listener::onSaveMovieClicked },
                    onClick = { listener::onMovieDetailsClicked },
                    modifier = Modifier.aspectRatio(0.8f),
                )
            }
        }
    }

    SavedListBottomSheet(
        isVisible = uiState.addToListBottomSheetState.isVisible,
        isUserLoggedIn = uiState.isUserLoggedIn,
        onAddClick = listener::onSaveItemToListClicked,
        onCreateNewListClick = listener::onCreateNewListClicked,
        onLoginClick = listener::onLoginClicked,
        onBottomSheetCloseClick = listener::onSaveToListBottomSheetDismiss,
        lists = savedLists,
        selectedListId = uiState.addToListBottomSheetState.selectedListId,
        onListSelected = listener::onListSelected,
    )
    AddListBottomSheet(
        isVisible = uiState.addListBottomSheetState.isVisible,
        isLoading = uiState.addListBottomSheetState.isLoading,
        listName = uiState.addListBottomSheetState.listName,
        onDismiss = listener::onCreateListBottomSheetDismiss,
        onAddClick = listener::onCreateListBottomSheetAddClick,
        onListNameChange = listener::onCreatedListNameChanged,
    )


}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int = type.toStringResource()
