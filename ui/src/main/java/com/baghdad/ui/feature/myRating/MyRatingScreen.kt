package com.baghdad.ui.feature.myRating

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.NovixTheme
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
import kotlinx.coroutines.flow.flowOf

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
                AnimatedContent(
                    targetState = mediaItems.itemCount != 0 && uiState.isLoading,
                ) { emptyRating ->
                    if (emptyRating)
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


private object FakeMyRatingInteractionListener : MyRatingInteractionListener {
    override fun onBackClick() {}
    override fun onMediaClick(mediaId: Long, contentType: MyRatingState.ContentType) {}
    override fun onMediaTabClick(mediaTab: MyRatingState.MediaTab?) {}
    override fun onDeleteClick(mediaId: Long, contentType: MyRatingState.ContentType) {}
    override fun onSnackBarActionLabelClick() {}
}

@Preview(showBackground = true)
@Composable
private fun MyRatingContentPreview() {
    val sampleMediaItems = listOf(
        MyRatingState.MediaItemUiState(
            id = 1,
            posterPictureURL = "https://via.placeholder.com/150",
            contentType = MyRatingState.ContentType.MOVIE,
            rating = 8
        ),
        MyRatingState.MediaItemUiState(
            id = 2,
            posterPictureURL = "https://via.placeholder.com/150",
            contentType = MyRatingState.ContentType.TV_SHOW,
            rating = 9
        )
    )

    val mediaFlow = remember { flowOf(PagingData.from(sampleMediaItems)) }
    val mediaItems = mediaFlow.collectAsLazyPagingItems()

    NovixTheme {
        MyRatingContent(
            uiState = MyRatingState(
                isLoading = false,
                selectedMediaTab = null,
                mediaFlow = mediaFlow
            ),
            listener = FakeMyRatingInteractionListener,
            snackBarState = SnackBarState(),
            mediaItems = mediaItems
        )
    }
}
