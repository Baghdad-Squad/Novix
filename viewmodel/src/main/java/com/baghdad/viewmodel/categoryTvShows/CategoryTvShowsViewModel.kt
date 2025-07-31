package com.baghdad.viewmodel.categoryTvShows

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsState.TvShowUiState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class CategoryTvShowsViewModel @Inject constructor(
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


    override fun onBackClicked() {
        sendEffect(CategoryTvShowsEffect.NavigateBack)
    }

    override fun onSavedClick(tvShowId: Long) {

    }

    override fun onTvShowClicked(tvShowId: Long) {
        sendEffect(CategoryTvShowsEffect.NavigateToTvShowDetails(tvShowId))
    }

    private fun getTvShowsByCategoryId(categoryId: Long) {
        collectPagingFlow(
            loadData = { page ->
                getTvShowsCategoryUseCase.invoke(categoryId, page)
            },
            onInitialLoadFinished = {},
            pageSize = 20,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = ::onGetTvShowsSuccess,
        )
    }

    private fun onGetTvShowsSuccess(tvShows: Flow<PagingData<TvShowUiState>>) {
        updateState { it.copy(tvShowsFlow = tvShows) }
    }

    private fun getCategoryNameById(categoryId: Long) {
        tryToExecute(
            callee = { getCategoryNameByIdUseCase.invoke(categoryId) },
            onSuccess = { onSuccessGetCategoryName(it.name) },
            onError = { onErrorGetCategoryName(it) }
        )
    }

    fun onSuccessGetCategoryName(categoryName: String) {
        updateState { it.copy(categoryName = categoryName) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    fun onErrorGetCategoryName(t: Throwable) {}


}