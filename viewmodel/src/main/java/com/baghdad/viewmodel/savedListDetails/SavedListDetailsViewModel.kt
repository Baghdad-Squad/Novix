package com.baghdad.viewmodel.savedListDetails

import androidx.lifecycle.SavedStateHandle
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavedListItem
import com.baghdad.domain.usecase.savedList.DeleteSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListDetailsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedListDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSavedListDetailsUseCase: GetSavedListDetailsUseCase,
    private val deleteSavedListUseCase: DeleteSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
) : BaseViewModel<SavedListDetailsScreenState, SavedListDetailsEffect>(SavedListDetailsScreenState()),
    SavedListDetailsInteractionListener {
    private val currentListId: Long = checkNotNull(savedStateHandle["listId"])

    init {
        getListDetails()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    override fun onBackClick() {
        sendEffect(SavedListDetailsEffect.NavigateBack)
    }

    private fun getListDetails() {
        hideSnackBar()
        collectPagingFlow(
            loadData = { page ->
                getSavedListDetailsUseCase.invoke(currentListId, page, 20).pagedItems
            },
            onInitialLoadFinished = ::onFinally,
            onInitialLoadError = ::onError,
            mapEntityToUiState = SavedListItem::toUIState,
            onFlowCreated = { mediaFlow ->
                updateState { it.copy(mediaFlow = mediaFlow) }
            },
            onLoadingChanged = ::onLoadingChanged
        )

        getSavedListInfo()
    }

    private fun getSavedListInfo() {
        tryToExecute(
            callee = { getSavedListDetailsUseCase.invoke(currentListId, 1, 1) },
            onSuccess = { result ->
                updateState {
                    it.copy(savedList = result.savedList.toUIState())
                }
            },
            onError = ::onError
        )
    }

    private fun onLoadingChanged(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    override fun onDeleteClick() {
        updateState {
            it.copy(
                isConfirmDeleteDialogVisible = true
            )
        }
    }

    override fun onMovieClick(mediaId: Long) {
        sendEffect(SavedListDetailsEffect.NavigateToMovieDetails(mediaId))
    }

    override fun onRemoveSavedMovieClick(movieId: Long) {
        tryToExecute(
            callee = { removeMovieFromSavedListUseCase(currentListId, movieId) },
            onSuccess = {
                showSnackBar(
                    message = BaseSnackBarMessage.RemovedItemSuccessfully,
                    isSuccess = true
                )
                refreshList()
            },
            onError = ::onError,
            onStart = { updateState { it.copy(isLoading = true) } },
            onFinally = { updateState { it.copy(isLoading = false) } }
        )
    }

    override fun onSnackBarActionLabelClick() {
        refreshList()
    }

    override fun onDeleteListBottomSheetDismiss() {
        updateState {
            it.copy(
                isConfirmDeleteDialogVisible = false
            )
        }
    }

    override fun onDeleteListBottomSheetDeleteClick() {
        tryToExecute(
            callee = { deleteSavedListUseCase(currentListId) },
            onSuccess = {
                showSnackBar(
                    message = BaseSnackBarMessage.DeleteListSuccessfully,
                    isSuccess = true
                )
                sendEffect(SavedListDetailsEffect.NavigateBack)
            },
            onError = ::onError,
            onStart = { updateState { it.copy(isLoading = true) } },
            onFinally = { updateState { it.copy(isLoading = false) } }
        )
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