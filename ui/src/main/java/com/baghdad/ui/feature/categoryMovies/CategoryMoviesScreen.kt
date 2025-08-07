package com.baghdad.ui.feature.categoryMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.paging.compose.LazyPagingItems
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
import com.baghdad.ui.feature.component.bottomSheet.AddListBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.SavedListBottomSheet
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent.NavigateToLogin
import com.baghdad.ui.navigation.graph.categories.CategoriesNavEvent.NavigateToMovieDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesEffect
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesState
import com.baghdad.viewmodel.categoryMovies.CategoryMoviesViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

@Composable
fun CategoryMoviesScreen(
    viewModel: CategoryMoviesViewModel = hiltViewModel(),
    handleNavigation: (CategoriesNavEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movieItems = uiState.moviesFlow.collectAsLazyPagingItems()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }


    CategoryMoviesContent(
        uiState = uiState,
        listener = viewModel,
        movieItems = movieItems,
        snackBarState = snackBarState
    )
}

private fun handleEffect(
    effect: CategoryMoviesEffect,
    handleNavigation: (CategoriesNavEvent) -> Unit,
) {
    when (effect) {
        is CategoryMoviesEffect.NavigateBack -> handleNavigation(
            CategoriesNavEvent.NavigateBack
        )

        is CategoryMoviesEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )

        CategoryMoviesEffect.NavigateToLogin -> handleNavigation(
            NavigateToLogin
        )
    }
}

@Composable
private fun CategoryMoviesContent(
    uiState: CategoryMoviesState,
    listener: CategoryMoviesViewModel,
    movieItems: LazyPagingItems<CategoryMoviesState.MovieUiState>,
    snackBarState: SnackBarState
) {
    val savedLists = uiState.addToListBottomSheetState.savedLists.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        isLoading = uiState.isLoading,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface)
                    .statusBarsPadding()
                    .padding(top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(R.drawable.ic_go_back),
                    onClick = { listener.onBackClick() },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 12.dp)
                )
                Text(
                    text = uiState.categoryName,
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
            }
        },
        snackbar = { position ->
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
                position = position,
            )
        },
        backgroundBlur = {
            BackgroundBlur()
        },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
    ) {
        Column {
            LazyPagingVerticalGrid<CategoryMoviesState.MovieUiState>(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                items = movieItems,
            ) { movie ->
                HomeCard(
                    url = movie.posterPictureURL,
                    contentDescription = null,
                    isSaved = movie.isSaved,
                    onSavedClick = { listener.onMovieToListClick(movie) },
                    onClick = { listener.onMovieClicked(movie.id) },
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }
        }
        SavedListBottomSheet(
            isVisible = uiState.addToListBottomSheetState.isVisible,
            isUserLoggedIn = uiState.isUserLoggedIn,
            onAddClick = listener::onSaveItemToListClick,
            onCreateNewListClick = listener::onCreateNewListClick,
            onLoginClick = listener::onLoginClick,
            onBottomSheetCloseClick = listener::onSaveToListBottomSheetDismiss,
            lists = savedLists ,
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
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
