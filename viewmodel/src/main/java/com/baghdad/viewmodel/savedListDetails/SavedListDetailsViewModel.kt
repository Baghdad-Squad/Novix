package com.baghdad.viewmodel.savedListDetails

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.savedList.DeleteSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListDetailsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import javax.inject.Inject

class SavedListDetailsViewModel @Inject constructor(
    private var currentListId: Long = 0,
    private val getSavedListDetailsUseCase: GetSavedListDetailsUseCase,
    private val deleteSavedListUseCase: DeleteSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase
) : BaseViewModel<SavedListDetailsScreenState, SavedListDetailsEffect>(SavedListDetailsScreenState()),
    SavedListDetailsInteractionListener {

    init {
        getListDetails()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    override fun onBackClick() {
        sendEffect(SavedListDetailsEffect.NavigateBack)
    }

    private fun getListDetails() {
        collectPagingFlow(
            loadData = { page ->
                val result = getSavedListDetailsUseCase(currentListId, page, 20)
                result.copy(
                    data = result.data.filter { it.type == com.baghdad.domain.model.savedList.SavedListItem.Type.MOVIE }
                )
            },
            onInitialLoadFinished = ::onFinally,
            onInitialLoadError = ::onError,
            mapEntityToUiState = { savedListItem ->
                SavedListDetailsScreenState.SavedListDetailsMovieUiState(
                    id = savedListItem.id,
                    posterUrl = savedListItem.posterUrl,
                    name = savedListItem.title,
                    contentType = SavedListDetailsScreenState.SavedListDetailsMovieUiState.ContentType.MOVIE
                )
            },
            onFlowCreated = { mediaFlow ->
                updateState { it.copy(mediaFlow = mediaFlow) }
            },
            onLoadingChanged = ::onLoadingChanged
        )

        getSavedListInfo()
    }

    private fun getSavedListInfo() {
        tryToExecute(
            callee = { getSavedListDetailsUseCase(currentListId, 1, 1) },
            onSuccess = { result ->
                updateState {
                    it.copy(savedList = result.data.)
                }
            },
            onError = ::onError
        )
    }

    private fun onLoadingChanged(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    override fun onDeleteClick(listId: Long) {
        tryToExecute(
            callee = { deleteSavedListUseCase(listId) },
            onSuccess = {
                showSnackBar(
                    message = BaseSnackBarMessage.DefaultMessage,
                    isSuccess = true
                )
                sendEffect(SavedListDetailsEffect.NavigateBack)
            },
            onError = ::onError,
            onStart = { updateState { it.copy(isLoading = true) } },
            onFinally = { updateState { it.copy(isLoading = false) } }
        )
    }

    override fun onCategoryClick(categoryId: Long) {
    }

    override fun onTvShowClick(tvShowId: Long) {
    }

    override fun onMovieClick(movieId: Long) {
        sendEffect(SavedListDetailsEffect.NavigateToMovieDetails(movieId))
    }

    override fun onRemoveSavedMovieClick(movieId: Long) {
        tryToExecute(
            callee = { removeMovieFromSavedListUseCase(currentListId, movieId) },
            onSuccess = {
                showSnackBar(
                    message = BaseSnackBarMessage.DefaultMessage,
                    isSuccess = true
                )
                refreshList()
            },
            onError = ::onError,
            onStart = { updateState { it.copy(isLoading = true) } },
            onFinally = { updateState { it.copy(isLoading = false) } }
        )
    }

    override fun onRemoveSavedTvShowClick(tvShowId: Long) {
    }

    override fun onSnackBarActionLabelClick() {
        refreshList()
    }

    private fun refreshList() {
        getListDetails()
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }

    private fun onError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun onFinally() {
        updateState { it.copy(isLoading = false) }
    }

}