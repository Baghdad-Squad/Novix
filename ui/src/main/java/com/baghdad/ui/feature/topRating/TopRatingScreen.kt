package com.baghdad.ui.feature.topRating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Tab
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.bottomSheet.AddListBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.SavedListBottomSheet
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.feature.topRating.component.GenresSection
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateBack
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToLogin
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.home.HomeNavEvent.NavigateToTvShowDetails
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import com.baghdad.viewmodel.topRating.TopRatingEffect
import com.baghdad.viewmodel.topRating.TopRatingInteractionListener
import com.baghdad.viewmodel.topRating.TopRatingState
import com.baghdad.viewmodel.topRating.TopRatingTab
import com.baghdad.viewmodel.topRating.TopRatingViewModel


@Composable
fun TopRatingScreen(
    viewModel: TopRatingViewModel = hiltViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit
) {
    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, handleNavigation)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val movieItems = uiState.moviesFlow.collectAsLazyPagingItems()
    val tvShowItems = uiState.tvShowsFlow.collectAsLazyPagingItems()

    TopRatingContent(
        uiState = uiState,
        listener = viewModel,
        snackBarState = snackBarState,
        movieItems = movieItems,
        tvShowItems = tvShowItems,
    )
}

private fun handleEffect(
    effect: TopRatingEffect,
    handleNavigation: (HomeNavEvent) -> Unit
) {
    when (effect) {

        TopRatingEffect.NavigateBack -> handleNavigation(
            NavigateBack
        )

        is TopRatingEffect.NavigateToMovieDetails -> handleNavigation(
            NavigateToMovieDetails(effect.movieId)
        )

        is TopRatingEffect.NavigateToTvShowDetails -> handleNavigation(
            NavigateToTvShowDetails(effect.tvShowId)
        )

        TopRatingEffect.NavigateToLogin -> handleNavigation(
            NavigateToLogin
        )
    }
}

@Composable
private fun TopRatingContent(
    uiState: TopRatingState,
    listener: TopRatingInteractionListener,
    snackBarState: SnackBarState,
    movieItems: LazyPagingItems<TopRatingState.MovieUiState>,
    tvShowItems: LazyPagingItems<TopRatingState.TvShowUiState>,
) {
    val movieGenresScrollState = rememberLazyListState()
    val tvGenresScrollState = rememberLazyListState()
    val savedLists = uiState.addToListBottomSheetState.savedLists.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .systemBarsPadding()
            .statusBarsPadding(),
        isLoading = uiState.isLoading,
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 22.dp, bottom = 8.dp)
                        .background(Theme.color.surface),
                    onGoBackClick = {
                        listener.onBackClick()
                    },
                    screenTitle = stringResource(com.baghdad.ui.R.string.top_rating),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val borderColor = Theme.color.stroke
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Theme.color.surface)
                            .padding(top = 4.dp)
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = borderColor,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Tab(
                            text = stringResource(com.baghdad.ui.R.string.movies),
                            onClick = { listener.onSelectedTab(TopRatingTab.MOVIES) },
                            isSelected = uiState.selectedTab == TopRatingTab.MOVIES,
                            modifier = Modifier.weight(1f)
                        )
                        Tab(
                            text = stringResource(com.baghdad.ui.R.string.tv_shows),
                            onClick = { listener.onSelectedTab(TopRatingTab.TV_SHOWS) },
                            isSelected = uiState.selectedTab == TopRatingTab.TV_SHOWS,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                GenresSection(
                    allGenres = uiState.genres,
                    selectedGenres = when (uiState.selectedTab) {
                        TopRatingTab.MOVIES -> uiState.selectedMovieGenreId
                        TopRatingTab.TV_SHOWS -> uiState.selectedTvShowGenreId
                    },
                    listState = when (uiState.selectedTab) {
                        TopRatingTab.MOVIES -> movieGenresScrollState
                        TopRatingTab.TV_SHOWS -> tvGenresScrollState
                    },
                    onGenreSelected = { listener.onGenreClick(it?.id) },
                    modifier = Modifier
                        .background(Theme.color.surface)
                        .padding(vertical = 12.dp)
                )
            }


        },
        snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible,
                actionLabel = snackBarState.actionLabelRes?.let { stringResource(it) },
                onActionClick = listener::onSnackBarActionLabelClick,
            )
        },
        backgroundBlur = {
            BackgroundBlur(modifier = Modifier.zIndex(999f))
        }) {

        when (uiState.selectedTab) {
            TopRatingTab.MOVIES -> {
                LazyPagingVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.color.surface),
                    contentPadding = PaddingValues(
                        bottom = 12.dp,
                        end = 16.dp,
                        start = 16.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    items = movieItems,
                ) { movie ->
                    Box(contentAlignment = Alignment.TopCenter) {

                    }

                    HomeCard(
                        url = movie.posterPictureURL,
                        contentDescription = null,
                        isSaved = movie.isSaved,
                        onSavedClick = { listener.onTopRatingItemSaveClick(movie) },
                        onClick = { listener.onMovieDetailsClick(movie.id) },
                        modifier = Modifier.aspectRatio(0.8f)
                    )
                }
            }

            TopRatingTab.TV_SHOWS -> {
                LazyPagingVerticalGrid<TopRatingState.TvShowUiState>(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.color.surface),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    items = tvShowItems,
                ) { tvShow ->
                    HomeCard(
                        url = tvShow.posterPictureURL,
                        contentDescription = null,
                        isSaved = tvShow.isSaved,
                        isSaveToListVisible = false,
                        onClick = { listener.onTvShowDetailsClick(tvShow.id) },
                        modifier = Modifier.aspectRatio(0.8f)
                    )
                }
            }
        }

        SavedListBottomSheet(
            isVisible = uiState.addToListBottomSheetState.isVisible,
            isUserLoggedIn = uiState.isUserLoggedIn,
            onAddClick = listener::onSaveMovieClick,
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
    return when (type) {
        SearchSnackBarMessage.SavedItemSuccessfully -> com.baghdad.ui.R.string.snackbar_saved_success
        else -> type.toStringResource()
    }
}
