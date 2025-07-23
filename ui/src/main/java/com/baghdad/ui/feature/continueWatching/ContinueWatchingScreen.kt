package com.baghdad.ui.feature.continueWatching

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.R
import com.baghdad.design_system.component.Chip
import com.baghdad.design_system.component.HomeCard
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.WavyLoadingIndicator
import com.baghdad.design_system.component.button.IconButton
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.lazyPaging.LazyPagingVerticalGrid
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.continueWatching.ContinueWatchingInteractionListener
import com.baghdad.viewmodel.continueWatching.ContinueWatchingScreenEffect
import com.baghdad.viewmodel.continueWatching.ContinueWatchingState
import com.baghdad.viewmodel.continueWatching.ContinueWatchingViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import org.koin.androidx.compose.koinViewModel


@Composable
fun ContinueWatchingScreen(
    viewModel: ContinueWatchingViewModel = koinViewModel(),
    handleNavigation: (HomeNavEvent) -> Unit,

    ) {
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val mediaItems = uiState.mediaFlow.collectAsLazyPagingItems()
    Log.d("ContinueWatchingScreen", "ContinueWatchingScreen: $uiState")

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is ContinueWatchingScreenEffect.NavigateBack -> handleNavigation(
                HomeNavEvent.NavigateBack
            )

            is ContinueWatchingScreenEffect.NavigateToLogin -> handleNavigation(
                HomeNavEvent.NavigateToLogin
            )

            is ContinueWatchingScreenEffect.NavigateToMovieDetails -> handleNavigation(
                HomeNavEvent.NavigateToMovieDetails(effect.movieId)
            )

            is ContinueWatchingScreenEffect.NavigateToTvShowDetails -> handleNavigation(
                HomeNavEvent.NavigateToTvShowDetails(effect.tvShowId)
            )
        }
    }
    ContinueWatchingContent(
        uiState = uiState,
        listener = viewModel,
        mediaItems = mediaItems,
        snackBarState = snackBarState,
    )
}

@Composable
fun ContinueWatchingContent(
    uiState: ContinueWatchingState,
    mediaItems: LazyPagingItems<ContinueWatchingState.ContinueWatchingMovieUiState>,
    listener: ContinueWatchingInteractionListener,
    snackBarState: SnackBarState,
) {
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.color.surface)
                    .statusBarsPadding()
                    .padding(top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    icon = painterResource(R.drawable.ic_go_back),
                    onClick = { listener.onBackClick() },
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Text(
                    text = "Continue watching",
                    style = Theme.typography.title.large,
                    color = Theme.color.title,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
            GenresTabs(
                genres = uiState.genres,
                selectedTab = uiState.selectedTab,
                onTabClick = { listener.onGenreClick(it) },
            )
        }, snackbar = {
            SnackBar(
                message = stringResource(snackBarMessage(snackBarState.message)),
                isSuccess = snackBarState.isSuccess,
                isVisible = snackBarState.isVisible
            )
        }
    ) {
        Column {

            if (uiState.isLoading) {
                Log.d("ContinueWatchingScreen", "ContinueWatchingContent: Loading")
                Box(Modifier.fillMaxSize()) {
                    WavyLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            LazyPagingVerticalGrid<ContinueWatchingState.ContinueWatchingMovieUiState>(
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
                items = mediaItems,
            ) { media ->

                HomeCard(
                    url = media.posterPictureURL,
                    contentDescription = null,
                    isSaved = media.isSaved,
                    onSavedClick = { listener.onMovieSaveClick(media.id) },
                    onClick = { listener.onMediaClick(media.id , media.contentType) },
                    modifier = Modifier.aspectRatio(0.8f)
                )
            }
        }
    }

}


@Composable
fun GenresTabs(
    genres: List<ContinueWatchingState.GenreUiState>,
    selectedTab: Long,
    onTabClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier
            .wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(genres.size) { index ->
            Chip(
                title = genres[index].name,
                isSelected = selectedTab == genres[index].id,
                onClick = { onTabClick(genres[index].id) }
            )
        }
    }
}

@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}
