package com.baghdad.viewmodel.myLists

import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class MyListsViewModel(
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val isUserLoggedInUseCase: IsLoggedInUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<MyListsScreenState, MyListsScreenEffect>(MyListsScreenState()),
    MyListsInteractionListener {

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        tryToExecute(
            callee = { isUserLoggedInUseCase() },
            onSuccess = ::handleLoginResult,
            dispatcher = defaultDispatcher,
            onError = ::onLoadDataError,
            onStart = ::onCheckIfUserLoggedInStart,
            onFinally = ::onCheckIfUserLoggedInFinished
        )
    }

    private fun handleLoginResult(isLoggedIn: Boolean) {
        updateState {
            it.copy(isUsedLoggedIn = isLoggedIn)
        }
        if (isLoggedIn) {
            getSavedLists()
        }
    }

    private fun getSavedLists() {
        collectPagingFlow(
            loadData = ::fetchSavedLists,
            onInitialLoadFinished = ::onInitialLoadSnackBarFinished,
            onInitialLoadError = { error -> handleError(error) },
            pageSize = PAGES_SIZE,
            mapEntityToUiState = SavedList::toUiState,
            onFlowCreated = ::handleSavedListsFlowCreated,
            onLoadingChanged = ::addSavedListLoadingChanged
        )
    }

    private fun handleSavedListsFlowCreated(flow: Flow<PagingData<MyListsScreenState.SavedListUiState>>) {
        updateState {
            it.copy(savedLists = flow)
        }
    }

    private suspend fun fetchSavedLists(page: Int): PagedResult<SavedList> {
        return getSavedListsUseCase(
            page = page,
            pageSize = PAGES_SIZE
        )
    }

    private fun addSavedListLoadingChanged(isLoading: Boolean) {
        updateState {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun onInitialLoadSnackBarFinished() {
        {
            updateState {
                it.copy(
                    savedLists = it.savedLists,
                )
            }
        }
    }

    private fun onLoadDataError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    private fun onCheckIfUserLoggedInStart() {
        updateState {
            it.copy(
                isUsedLoggedIn = true
            )
        }
    }

    private fun onCheckIfUserLoggedInFinished() {
        updateState {
            it.copy(
                isUsedLoggedIn = false
            )
        }
    }

    private fun showNoInternetSnackBar() {
        updateState {
            it.copy(
            )
        }
        hideSnackBar()
    }

    override fun onAddListFabClick() {
        updateState {
            it.copy(
                addListBottomSheetState = MyListsScreenState.AddListBottomSheetState(
                    isVisible = true,
                )
            )
        }
    }

    override fun onLoginClick() {
        sendEffect(MyListsScreenEffect.NavigateToLogin)
    }

    override fun onAddedListNameChange(name: String) {
        updateState {
            it.copy(
                addListBottomSheetState = it.addListBottomSheetState.copy(
                    listName = name
                )
            )
        }
    }

    override fun onAddListBottomSheetDismiss() {
        updateState {
            it.copy(
                addListBottomSheetState = it.addListBottomSheetState
            )
        }
    }

    override fun onAddListBottomSheetAddClick() {
        val listName = uiState.value.addListBottomSheetState.listName

        ::setBottomSheetLoading

        tryToExecute(
            callee = { createSavedListUseCase(listName) },
            dispatcher = defaultDispatcher,
            onSuccess = { hideAddListBottomSheet() },
            onError = ::handleAddListError
        )
    }

    private fun handleAddListError(error: Throwable) {
        handleError(error)
        setBottomSheetLoading(false)
    }


    private fun hideAddListBottomSheet() {
        updateState {
            it.copy(
                addListBottomSheetState = MyListsScreenState.AddListBottomSheetState(
                    isVisible = false
                )
            )
        }
    }

    private fun setBottomSheetLoading(isLoading: Boolean) {
        updateState {
            it.copy(
                addListBottomSheetState = it.addListBottomSheetState.copy(isLoading = isLoading)
            )
        }
    }

    override fun onListClick(listId: Long) {
        sendEffect(MyListsScreenEffect.NavigateToViewSavedDetails(listId))
    }

    override fun onSnackBarActionLabelClick() {
        getSavedLists()
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.UnknownError
    }

    companion object {
        private const val PAGES_SIZE = 20
    }
}