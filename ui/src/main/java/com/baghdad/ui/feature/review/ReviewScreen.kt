package com.baghdad.ui.feature.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.BackgroundBlur
import com.baghdad.design_system.component.Scaffold
import com.baghdad.design_system.component.SnackBar
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.base.toStringResource
import com.baghdad.ui.feature.review.component.ReviewerCard
import com.baghdad.ui.feature.search.component.EmptySearchState
import com.baghdad.ui.navigation.graph.reviews.ReviewsNavEvent
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.SearchSnackBarMessage
import com.baghdad.viewmodel.review.ReviewInteractionListener
import com.baghdad.viewmodel.review.ReviewScreenEffect
import com.baghdad.viewmodel.review.ReviewScreenState
import com.baghdad.viewmodel.review.ReviewViewModel


@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel = hiltViewModel(),
    onNavEvent: (ReviewsNavEvent) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    ReviewContent(uiState = uiState, listener = viewModel, snackBarState = snackBarState)

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is ReviewScreenEffect.NavigateBack -> onNavEvent(ReviewsNavEvent.NavigateBack)
        }
    }
}

@Composable
private fun ReviewContent(
    uiState: ReviewScreenState,
    listener: ReviewInteractionListener,
    snackBarState: SnackBarState
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),

        topBar = {
            TopAppBar(
                screenTitle = stringResource(R.string.reviews),
                onGoBackClick = { listener.onNavigateBack() },
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
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

        isLoading = uiState.isLoading,

        backgroundBlur = {
            BackgroundBlur()
        },

        isSnackBarWithActionLabel = snackBarState.actionLabelRes != null

        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (uiState.reviews.isEmpty()) {
                EmptyReviewScreen()
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(uiState.reviews) { review ->
                        ReviewerCard(
                            title = review.reviewText,
                            rate = review.rating,
                            authorName = review.authorName,
                            reviewDate = review.postedDate,
                            authorAvatar = review.authorAvatarUrl,
                            contentName = review.contentTitle,
                            isExpanded = review.isExpanded,
                            onExpandedChange = {listener.onExpandedTextChange(review.id) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun snackBarMessage(type: BaseSnackBarMessage): Int {
    return when (type) {
        SearchSnackBarMessage.RemovedItemSuccessfully -> R.string.snackbar_removed_success
        SearchSnackBarMessage.SavedItemSuccessfully -> R.string.snackbar_saved_success
        else -> type.toStringResource()
    }
}

@Composable
private fun EmptyReviewScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        contentAlignment = Alignment.Center
    ) {
        EmptySearchState(
            imagePath = Theme.drawable.emptyReviews,
            contentDescription = stringResource(R.string.there_is_no_review),
            message = stringResource(R.string.there_is_no_review)
        )
    }
}