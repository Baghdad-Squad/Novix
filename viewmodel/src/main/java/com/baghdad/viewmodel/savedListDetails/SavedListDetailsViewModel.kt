package com.baghdad.viewmodel.savedListDetails

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.savedList.SavedListItem
import com.baghdad.domain.usecase.savedList.DeleteSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListDetailsUseCase
import com.baghdad.domain.usecase.savedList.RemoveMovieFromSavedListUseCase
import com.baghdad.domain.usecase.savedList.RemoveTvShowFromSavedListUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.savedListDetails.SavedListDetailsScreenState.SavedListDetailsMovieUiState.ContentType
import javax.inject.Inject

class SavedListDetailsViewModel @Inject constructor(
    private var currentListId: Long = 0,
    private val getSavedListDetailsUseCase: GetSavedListDetailsUseCase,
    private val deleteSavedListUseCase: DeleteSavedListUseCase,
    private val removeMovieFromSavedListUseCase: RemoveMovieFromSavedListUseCase,
    private val removeTvShowFromSavedListUseCase: RemoveTvShowFromSavedListUseCase
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

    override fun onCategoryClick(category: SavedListTab) {
        updateState { it.copy(selectedTab = category) }
        refreshList()
    }

    override fun onMediaClick(
        mediaId: Long,
        contentType: ContentType
    ) {
        when (contentType) {
            ContentType.MOVIE ->
                sendEffect(SavedListDetailsEffect.NavigateToMovieDetails(mediaId))

            ContentType.TV_SHOW ->
                sendEffect(SavedListDetailsEffect.NavigateToTvShowDetails(mediaId))
        }

    }

    override fun onRemoveSavedMediaClick(mediaId: Long, contentType: ContentType) {
        when (contentType) {
            ContentType.MOVIE ->
                onRemoveSavedMovieClick(mediaId)

            ContentType.TV_SHOW ->
                onRemoveSavedTvShowClick(mediaId)
        }
    }


    private fun onRemoveSavedMovieClick(movieId: Long) {
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

    private fun onRemoveSavedTvShowClick(tvShowId: Long) {
        tryToExecute(
            callee = { removeTvShowFromSavedListUseCase(currentListId, tvShowId) },
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