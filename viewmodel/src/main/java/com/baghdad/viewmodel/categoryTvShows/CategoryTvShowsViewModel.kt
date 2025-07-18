package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.domain.usecase.category.GetCategoryTvShowsUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class CategoryTvShowsViewModel(
    private val categoryId: Long,
    private val getTvShowsCategoryUseCase: GetCategoryTvShowsUseCase
) : BaseViewModel<CategoryTvShowsState, CategoryTvShowsEffect>(CategoryTvShowsState()),
    CategoryTvShowsInteractionListener {

    init {
        getTvShowsByCategoryId(categoryId = categoryId)
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    override fun onBackClicked() {
        sendEffect(CategoryTvShowsEffect.NavigateBack)
    }

    override fun onSavedClick(tvShowId: Long) {
        updateState {
            it.copy(
                tvShows = it.tvShows.map { item ->
                    if (item.id == tvShowId) item.copy(isSaved = item.isSaved.not()) else item
                }
            )
        }
    }

    private fun getTvShowsByCategoryId(categoryId: Long) {
        tryToExecute(
            callee = { getTvShowsCategoryUseCase.invoke(categoryId = categoryId) },
            onSuccess = { ::onSuccess },
            onError = { ::onError }

        )
    }

    private fun onSuccess() {}
    private fun onError() {}

}
