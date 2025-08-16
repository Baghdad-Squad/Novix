package com.baghdad.ui.feature.movieDetails

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.SaveIcon
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.Text
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.component.DetailsScreenBottomBar
import com.baghdad.ui.feature.component.HomeCard
import com.baghdad.ui.feature.component.bottomSheet.AddListBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.LoginRequiredSheet
import com.baghdad.ui.feature.component.bottomSheet.RatingBottomSheet
import com.baghdad.ui.feature.component.bottomSheet.SavedListBottomSheet
import com.baghdad.ui.feature.movieDetails.component.ActorsSection
import com.baghdad.ui.feature.movieDetails.component.MovieHeaderWithDetailsCard
import com.baghdad.ui.feature.movieDetails.component.OverviewSection
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateBack
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToActorDetails
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToCategoryMovies
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToLogin
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToMovieDetails
import com.baghdad.ui.navigation.graph.movieDetails.MovieDetailsNavEvent.NavigateToReviews
import com.baghdad.ui.util.openYouTubeLink
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.movieDetails.MovieDetailsEffect
import com.baghdad.viewmodel.movieDetails.MovieDetailsInteractionListener
import com.baghdad.viewmodel.movieDetails.MovieDetailsState
import com.baghdad.viewmodel.movieDetails.MovieDetailsViewModel
import com.baghdad.viewmodel.shared.BottomSheetType
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    handleNavigation: (MovieDetailsNavEvent) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        handleEffect(effect, context, handleNavigation)
    }

    MovieDetailsContent(
        listener = viewModel,
        state = state,
        snackBarState = snackBarState,

        )
}

private fun handleEffect(
    effect: MovieDetailsEffect,
    context: Context,
    handleNavigation: (MovieDetailsNavEvent) -> Unit,
) {
    when (effect) {
        is MovieDetailsEffect.NavigateToActorDetails -> {
            handleNavigation(
                NavigateToActorDetails(
                    actorId = effect.id
                )
            )
        }

        is MovieDetailsEffect.NavigateToCategory -> {
            handleNavigation(
                NavigateToCategoryMovies(
                    categoryId = effect.id
                )
            )
        }

        is MovieDetailsEffect.NavigateToMovie -> handleNavigation(
            NavigateToMovieDetails(
                movieId = effect.id,
            )
        )

        is MovieDetailsEffect.NavigateToReviewDetails -> handleNavigation(
            NavigateToReviews(
                movieId = effect.id
            )
        )

        MovieDetailsEffect.NavigateBack -> handleNavigation(
            NavigateBack
        )

        is MovieDetailsEffect.OpenYoutubeLink -> openYouTubeLink(context, effect.youtubeLink)

        MovieDetailsEffect.NavigateToLogin -> handleNavigation(
            NavigateToLogin
        )
    }
}


@Composable
private fun MovieDetailsContent(
    listener: MovieDetailsInteractionListener,
    state: MovieDetailsState,
    snackBarState: SnackBarState
) {
    val savedLists = state.addToListBottomSheetState.savedLists.collectAsLazyPagingItems()
    val lazyState = rememberLazyStaggeredGridState()
    var shouldShowBackground by remember { mutableStateOf(false) }

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (shouldShowBackground) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = stringResource(R.string.background_alpha)
    )

    val animatedColor by animateColorAsState(
        targetValue = if (shouldShowBackground)
            Theme.color.surface
        else
            Theme.color.surface.copy(alpha = 0f),
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
    )

    LaunchedEffect(lazyState) {
        snapshotFlow {
            val firstVisibleItemIndex = lazyState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = lazyState.firstVisibleItemScrollOffset

            if (firstVisibleItemIndex > 0) {
                Int.MAX_VALUE
            } else {
                firstVisibleItemScrollOffset
            }
        }.collect { totalScrollPosition ->
            shouldShowBackground = totalScrollPosition > 450
        }
    }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    Scaffold(
        modifier = Modifier
            .background(Theme.color.surface)
            .navigationBarsPadding(),
        isLoading = state.isLoading,
        bottomBar = {
            DetailsScreenBottomBar(
                hasTrailer = state.movieTrailerURL.isNotBlank(),
                onRateClicked = { listener.onClickStarButton() },
                onPlayTrailerClicked = { listener.onClickPlayTrailer() },
                isRated = state.isRated,
                isLoading = false
            )
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
        backgroundBlur = { BackgroundBlur() },
        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null,
    ) {

        Box(
            modifier = Modifier
                .background(Theme.color.surface.copy(backgroundAlpha))
                .fillMaxSize()
                .navigationBarsPadding()

        ) {

            LazyVerticalStaggeredGrid(
                state = lazyState,
                columns = StaggeredGridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize(),
                verticalItemSpacing = 12.dp,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {

                item(span = StaggeredGridItemSpan.FullLine) {
                    MovieHeaderWithDetailsCard(
                        uiState = state,
                        listener = listener,
                        modifier = Modifier.ignoreHorizontalPadding()
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(Modifier.height(104.dp))
                }

                if (state.overView.isNotBlank()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        OverviewSection(
                            overview = state.overView,
                            isExtended = state.isExtendText,
                            onExtendClicked = listener::onExtendOverviewClick
                        )
                    }
                }

                if (state.castMembers.isNotEmpty()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        ActorsSection(
                            actors = state.castMembers,
                            onClick = { listener.onActorClick(id = it) },
                            modifier = Modifier.ignoreHorizontalPadding()
                        )
                    }
                }

                if (state.moreLikeThisMovie.isNotEmpty()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Text(
                            text = stringResource(R.string.more_like_this),
                            fontSize = 18.sp,
                            style = Theme.typography.title.medium,
                            color = Theme.color.title,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }

                    items(items = state.moreLikeThisMovie) { movie ->
                        HomeCard(
                            url = movie.imageUrl,
                            contentDescription = stringResource(R.string.card_movie_image),
                            isSaved = movie.isSaved,
                            onSavedClick = { listener.onSaveMoreLikeThisMedia(movie) },
                            onClick = { listener.onMovieClick(movie.id) },
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                .aspectRatio(0.75f)
                        )
                    }
                }

            }

            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(animatedColor)
                    .zIndex(1f)
                    .padding(top = 56.dp, bottom = 8.dp),
                onGoBackClick = listener::onBackClick,
                content = {
                    SaveIcon(
                        tint = Theme.color.title,
                        size = 40,
                        backgroundColor = Theme.color.iconBackgroundLow,
                        isSaved = state.isSaved,
                        onClick = { listener.onSaveCurrentMovieClick() }
                    )
                }
            )
        }

    }



    RatingBottomSheet(
        isVisible = state.ratingStatus.isBottomSheetVisible && state.ratingStatus.bottomSheetType
                == BottomSheetType.ShowRating,
        onBottomSheetCloseClick = { listener.onDismissRatingBottomSheet() },
        rate = state.userRating,
        onRateChanged = listener::onRatingChanged,
        isButtonEnabled = state.userRating != 0,
        onSubmitClick = { listener.onClickSubmitRating(rating = state.userRating) }
    )

    LoginRequiredSheet(
        isVisible = state.ratingStatus.isBottomSheetVisible && state.ratingStatus.bottomSheetType
                == BottomSheetType.RequireLogin,
        onBottomSheetCloseClick = { listener.onDismissRatingBottomSheet() },
        onLoginClick = { listener.onLoginClick() },
        title = stringResource(R.string.rate_it),
        description = stringResource(R.string.please_login_to_rate)
    )

    SavedListBottomSheet(
        isVisible = state.addToListBottomSheetState.isVisible,
        isUserLoggedIn = state.isUserLoggedIn,
        onAddClick = listener::onSaveItemToListClicked,
        onCreateNewListClick = listener::onCreateNewListClicked,
        onLoginClick = listener::onLoginClick,
        onBottomSheetCloseClick = listener::onSaveToListBottomSheetDismiss,
        lists = savedLists,
        selectedListId = state.addToListBottomSheetState.selectedListId,
        onListSelected = listener::onListSelected,
    )

    AddListBottomSheet(
        isVisible = state.addListBottomSheetState.isVisible,
        isLoading = state.addListBottomSheetState.isLoading,
        listName = state.addListBottomSheetState.listName,
        onDismiss = listener::onCreateListBottomSheetDismiss,
        onAddClick = listener::onCreateListBottomSheetAddClick,
        onListNameChange = listener::onCreatedListNameChanged,
    )
}

private fun Modifier.ignoreHorizontalPadding(extra: Dp = 32.dp): Modifier =
    this.then(
        Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(
                constraints.copy(
                    maxWidth = constraints.maxWidth + extra.roundToPx()
                )
            )
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
    )


@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return type.toStringResource()
}