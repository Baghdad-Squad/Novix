package com.baghdad.viewmodel.categoryTvShows

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.genre.GetTvShowGenreNameByIdUseCase
import com.baghdad.domain.usecase.tvShow.GetTvShowsByGenreUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.categoryTvShows.CategoryTvShowsState.TvShowUiState
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class CategoryTvShowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTvShowsCategoryUseCase: GetTvShowsByGenreUseCase,
    private val getCategoryNameByIdUseCase: GetTvShowGenreNameByIdUseCase,
    private val ioDispatcher: CoroutineDispatcher
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

    override fun onTvShowClicked(tvShowId: Long) {
        sendEffect(CategoryTvShowsEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onSnackBarActionLabelClick() {
        getTvShowsByCategoryId(categoryId = categoryId)
        getCategoryNameById(categoryId = categoryId)
    }

    private fun getTvShowsByCategoryId(categoryId: Long) {
        collectPagingFlow(
            loadData = { page ->
                getTvShowsCategoryUseCase.invoke(categoryId, page)
            },
            onInitialLoadFinished = {
                updateState { it.copy(isLoading = false) }
            },
            pageSize = 20,
            mapEntityToUiState = { it.toUiState() },
            onFlowCreated = ::onGetTvShowsSuccess,
            onLoadingChanged = { isLoading->
                updateState { it.copy(isLoading = isLoading) }
            },
            onInitialLoadError = ::onLoadDataError
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

    private fun onGetTvShowsSuccess(tvShows: Flow<PagingData<TvShowUiState>>) {
        hideSnackBar()
        updateState { it.copy(tvShowsFlow = tvShows) }
    }

    private fun getCategoryNameById(categoryId: Long) {
        tryToExecute(
            callee = { getCategoryNameByIdUseCase.invoke(categoryId) },
            dispatcher = ioDispatcher,
            onSuccess = { onSuccessGetCategoryName(it.name) },
            onError = ::onLoadDataError
        )
    }

    fun onSuccessGetCategoryName(categoryName: String) {
        updateState { it.copy(categoryName = categoryName) }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }
}