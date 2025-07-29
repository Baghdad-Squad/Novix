package com.baghdad.viewmodel.categoryTvShows

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryTvShowsViewModel @Inject constructor(
//    private val categoryId: Long,
    savedStateHandle: SavedStateHandle,

    private val getTvShowsCategoryUseCase: GetTvShowsByGenreUseCase,
    private val getCategoryNameByIdUseCase: GetTvShowGenreNameByIdUseCase
) : BaseViewModel<CategoryTvShowsState, CategoryTvShowsEffect>(CategoryTvShowsState()),
    CategoryTvShowsInteractionListener {

    private val categoryId: Long = checkNotNull(savedStateHandle["categoryId"])
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

    override fun onTvShowClicked(tvShowId: Long) {
        sendEffect(CategoryTvShowsEffect.NavigateToTvShowDetails(tvShowId))
    }

    private fun getTvShowsByCategoryId(categoryId: Long) {
        tryToExecute(
            callee = {
                getTvShowsCategoryUseCase.invoke(genreId = categoryId, page = 1)
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