package com.baghdad.viewmodel.review

import com.baghdad.domain.usecase.review.GetMovieReviewsUseCase
import com.baghdad.domain.usecase.review.GetTvShowReviewsUseCase
import com.baghdad.entity.media.Review
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getSeriesReviewsUseCase: GetTvShowReviewsUseCase,
    val contentId: Long,
    contentType: ContentType,
) : BaseViewModel<ReviewScreenState, ReviewScreenEffect>(ReviewScreenState()),
    ReviewInteractionListener {


    init {
        when (contentType) {
            ContentType.MOVIE -> loadReviewsForMovie()
            ContentType.SERIES -> loadReviewsForSeries()
        }
    }

    private fun loadReviewsForMovie() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getMovieReviewsUseCase(contentId) },
            onSuccess = ::onReviewsSuccess,
            onFinally = ::onFinally
        )
    }

    fun loadReviewsForSeries() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getSeriesReviewsUseCase(contentId) },
            onSuccess = ::onReviewsSuccess,
            onFinally = ::onFinally
        )
    }

    private fun onReviewsSuccess(reviews: List<Review>) {
        val uiStates = reviews.map { it.toReviewUi() }
        updateState { it.copy(reviews = uiStates, isLoading = false) }
    }


    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onNavigateBack() {
        sendEffect(ReviewScreenEffect.NavigateBack)
    }

    override fun onExpandedTextChange(id: String) {
        updateState { currentState ->
            val updatedReviews = currentState.reviews.map { review ->
                if (review.id == id) {
                    review.copy(isExpanded = !review.isExpanded)
                } else {
                    review
                }
            }
            currentState.copy(reviews = updatedReviews)
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}