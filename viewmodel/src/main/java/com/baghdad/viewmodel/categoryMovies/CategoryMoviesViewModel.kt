package com.baghdad.viewmodel.categoryMovies

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class CategoryMoviesViewModel(
    private val categoryId: Long,
) : BaseViewModel<CategoryMoviesState, CategoryMoviesEffect>(CategoryMoviesState()),
    CategoryMoviesInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onBackClicked() {
        sendEffect(CategoryMoviesEffect.NavigateBack)
    }


}
