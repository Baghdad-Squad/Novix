package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.domain.usecase.category.GetCategoryTvShowsUseCase
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class CategoryTvShowsViewModel(
    private val categoryId: Long,
    private val getTvShowsCategoryUseCase: GetCategoryTvShowsUseCase,
    private val getCategoryNameByIdUseCase: GetTvShowGenreNameByIdUseCase
) : BaseViewModel<CategoryTvShowsState, CategoryTvShowsEffect>(CategoryTvShowsState()),
    CategoryTvShowsInteractionListener {

    init {
        getTvShowsByCategoryId(categoryId = categoryId)
        getCategoryNameById(categoryId = categoryId)
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
            callee = {
                getTvShowsCategoryUseCase.invoke(categoryId = categoryId)
            },
            onSuccess = { onGetTvShowsSuccess(it) },
            onError = { onGetTvShowsError() }

        )
    }

    private fun onGetTvShowsSuccess(tvShows: List<TvShow>) {
        updateState { it.copy(tvShows = tvShows.map { it.toUiState() }) }
    }

    private fun onGetTvShowsError() {}

    private fun getCategoryNameById(categoryId: Long) {
        tryToExecute(
            callee = { getCategoryNameByIdUseCase.invoke(categoryId) },
            onSuccess = { onSuccessGetCategoryName(it.name) },
            onError = { onErrorGetCategoryName() }
        )
    }

    fun onSuccessGetCategoryName(categoryName: String) {
        updateState { it.copy(categoryName = categoryName) }
    }

    fun onErrorGetCategoryName() {}

}