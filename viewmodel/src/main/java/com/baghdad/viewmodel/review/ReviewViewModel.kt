package com.baghdad.viewmodel.review

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.review.GetMovieReviewsUseCase
import com.baghdad.domain.usecase.review.GetTvShowReviewsUseCase
import com.baghdad.entity.media.Review
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getSeriesReviewsUseCase: GetTvShowReviewsUseCase,
    savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<ReviewScreenState, ReviewScreenEffect>(ReviewScreenState()),
    ReviewInteractionListener {

        private val contentId: Long = checkNotNull(savedStateHandle["mediaId"])
        private val contentType : ContentType = checkNotNull(savedStateHandle["mediaType"])

    init {
        loadData()
    }

    private fun loadData() {
        when (contentType) {
            ContentType.MOVIE -> loadReviewsForMovie()
            ContentType.SERIES -> loadReviewsForSeries()
        }
    }

    private fun loadReviewsForMovie() {
        tryToExecute(
            onStart = ::onStart,
            callee = { getMovieReviewsUseCase(contentId) },
            onSuccess = ::onReviewsSuccess,
            onFinally = ::onFinally,
            onError = ::onLoadDataError
        )
    }

    fun loadReviewsForSeries() {
        tryToExecute(
            onStart = { updateState { it.copy(isLoading = true) } },
            callee = { getSeriesReviewsUseCase(contentId) },
            onSuccess = ::onReviewsSuccess,
            onFinally = ::onFinally,
            onError = ::onLoadDataError
        )
    }

    private fun onLoadDataError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun onReviewsSuccess(reviews: List<Review>) {
        hideSnackBar()
        val uiStates = reviews.map { it.toReviewUi() }
        updateState { it.copy(reviews = uiStates, isLoading = false) }
    }

    private fun onStart(){
        updateState { it.copy(isLoading = true) }
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

    override fun onSnackBarActionLabelClick() {
        loadData()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}