package com.baghdad.viewmodel.savedListDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.savedList.DeleteSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListDetailsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsScreenState.SavedListDetailsMovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SavedListDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSavedListDetailsUseCase: GetSavedListDetailsUseCase,
    private val deleteSavedListUseCase: DeleteSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<SavedListDetailsScreenState, SavedListDetailsEffect>(SavedListDetailsScreenState()),
    SavedListDetailsInteractionListener {
    private val currentListId: Long = checkNotNull(savedStateHandle["listId"])

    init {
        getListDetails()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError

    override fun onBackClick() {
        sendEffect(SavedListDetailsEffect.NavigateBack(false))
    }

    private fun getListDetails() {
        collectPagingFlow(
            loadData = { page ->
                getSavedListDetailsUseCase.invoke(
                    listId = currentListId,
                    page = page,
                    pageSize = PAGE_SIZE
                ).pagedItems
            },
            onInitialLoadFinished = ::onFinally,
            onInitialLoadError = ::onError,
            mapEntityToUiState = SavedMovie::toUIState,
            onFlowCreated = ::onFlowCreatedSuccessfully,
            onLoadingChanged = ::onLoadingChanged
        )

        getSavedListInfo()
    }

    private fun onFlowCreatedSuccessfully(mediaFlow: Flow<PagingData<SavedListDetailsMovieUiState>>) {
        updateState { it.copy(mediaFlow = mediaFlow) }
    }

    private fun getSavedListInfo() {
        tryToExecute(
            dispatcher = defaultDispatcher,
            callee = { getSavedListDetailsUseCase.invoke(currentListId, 1, 1) },
            onSuccess = ::onSuccessGetSavedListInfo,
            onError = ::onError
        )
    }

    private fun onSuccessGetSavedListInfo(savedListDetails: SavedListDetails) {
        updateState { it.copy(savedList = savedListDetails.savedList.toUIState()) }

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
            dispatcher = defaultDispatcher,
            callee = { removeMovieFromSavedListUseCase(currentListId, movieId) },
            onSuccess = { onSuccessRemoveSavedMovieClick() },
            onError = ::onError,
            onStart = ::startLoading
        )
    }

    private fun onSuccessRemoveSavedMovieClick() {
        showSnackBar(
            message = BaseSnackBarMessage.RemovedItemSuccessfully,
            isSuccess = true
        )
        refreshList()
    }

    override fun onSnackBarActionLabelClick() {
        hideSnackBar()
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
            dispatcher = defaultDispatcher,
            callee = { deleteSavedListUseCase(currentListId) },
            onSuccess = { onSuccessDeleteListClick() },
            onStart = ::startLoading,
            onFinally = ::stopLoading
        )
    }

    private fun onSuccessDeleteListClick() {
        sendEffect(SavedListDetailsEffect.NavigateBack(true))
    }

    private fun startLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun stopLoading() {
        updateState { it.copy(isLoading = false) }
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

    private companion object {
        const val PAGE_SIZE = 20
    }
}