package com.baghdad.viewmodel.topMoviePicks

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class TopMoviePicksViewModel(

): BaseViewModel<TopMoviePicksState, TopMoviePicksEffect>
    (TopMoviePicksState(isLoading = false)), TopMoviePicksInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }

    override fun onMovieDetailsClicked(movieId: Long) {
        TODO("Not yet implemented")
    }

    override fun onSaveMovieClicked(movieId: Long) {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }
}
