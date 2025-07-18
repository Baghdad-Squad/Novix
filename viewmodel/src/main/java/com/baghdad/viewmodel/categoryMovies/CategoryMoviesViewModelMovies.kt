package com.baghdad.viewmodel.categoryMovies

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class CategoryMoviesViewModelMovies(
    private val categoryId: Long,
) : BaseViewModel<CategoryMoviesState, CategoryMoviesEffect>(CategoryMoviesState()),
    CategoryMoviesInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        // return BaseSnackBarMessage(throwable.message ?: "Something went wrong", isSuccess = false, throwable = throwable)
    }

    override fun onBackClicked() {
        sendEffect(CategoryMoviesEffect.NavigateBack)
    }


}
