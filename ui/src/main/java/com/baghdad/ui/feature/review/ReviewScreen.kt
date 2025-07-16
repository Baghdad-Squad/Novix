package com.baghdad.ui.feature.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baghdad.design_system.component.appBar.TopAppBar
import com.baghdad.design_system.theme.Theme
import com.baghdad.ui.R
import com.baghdad.ui.base.ObserveAsEffect
import com.baghdad.ui.feature.review.component.ReviewerCard
import com.baghdad.ui.navigation.graph.reviews.ReviewsNavEvent
import com.baghdad.viewmodel.review.ContentType
import com.baghdad.viewmodel.review.ReviewInteractionListener
import com.baghdad.viewmodel.review.ReviewScreenEffect
import com.baghdad.viewmodel.review.ReviewScreenState
import com.baghdad.viewmodel.review.ReviewViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun ReviewScreen(
    mediaType: ContentType,
    mediaId: Long,
    viewModel: ReviewViewModel = koinViewModel(parameters = { parametersOf(mediaId, mediaType) }),
    onNavEvent: (ReviewsNavEvent) -> Unit
) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewContent(uiState = uiState, listener = viewModel)

    ObserveAsEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is ReviewScreenEffect.NavigateBack -> onNavEvent(ReviewsNavEvent.NavigateBack)
        }
    }
}

@Composable
fun ReviewContent(
    uiState: ReviewScreenState, listener: ReviewInteractionListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Theme.color.surface)

    ) {
        TopAppBar(
            screenTitle = stringResource(R.string.reviews),
            onGoBackClick = { listener.onNavigateBack() },
            modifier = Modifier.padding(top = 12.dp)
        ) {}

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surface)
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
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    listener.onExpandedTextChange(review.id)
                }
            }
        }
    }
}
