package com.baghdad.viewmodel.myLists

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.usecase.login.IsUserLoggedInUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import com.baghdad.viewmodel.errorStates.MyListsSnackBarMessage
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyListsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getSavedListsUseCase: GetSavedListsUseCase,
        private val createSavedListUseCase: CreateSavedListUseCase,
        private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
        private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : BaseViewModel<MyListsScreenState, MyListsScreenEffect>(MyListsScreenState()),
        MyListsInteractionListener {

        private val isDeleteSuccess: Boolean = checkNotNull(savedStateHandle["isDeleteSuccess"])

        init {
            checkIfUserIsLoggedIn()
            handleDeleteSuccess()
        }

        private fun handleDeleteSuccess() {
            if (isDeleteSuccess) {
                updateState { it.copy(shouldShowDeleteSuccessSnackBar = true) }
            }
        }

        private fun checkIfUserIsLoggedIn() {
            tryToExecute(
                callee = { isUserLoggedInUseCase() },
                onSuccess = ::onCheckIfUserIsLoggedInSuccess,
                dispatcher = defaultDispatcher,
            )
        }

        private fun onCheckIfUserIsLoggedInSuccess(isLoggedIn: Boolean) {
            updateState {
                it.copy(
                    isUsedLoggedIn = isLoggedIn,
                    isLoading = isLoggedIn,
                )
            }
            if (isLoggedIn) {
                getSavedLists()
            }
        }

        private fun getSavedLists() {
            collectPagingFlow(
                loadData = ::fetchSavedLists,
                onInitialLoadError = ::onLoadDataError,
                pageSize = PAGES_SIZE,
                mapEntityToUiState = SavedList::toUiState,
                onFlowCreated = ::onSavedListsFlowCreated,
                onLoadingChanged = ::onGetSavedListsLoadingChanged,
            )
        }

    private suspend fun fetchSavedLists(page: Int): PagedResult<SavedList> {
        return getSavedListsUseCase(
                page = page,
                pageSize = PAGES_SIZE,
            )
    }


        private fun onLoadDataError(throwable: Throwable) {
            when (throwable) {
                is NoInternetException -> showNoInternetSnackBar()
                else -> handleError(throwable)
            }
        }

        private fun onSavedListsFlowCreated(flow: Flow<PagingData<MyListsScreenState.SavedListUiState>>) {
            updateState {
                it.copy(savedLists = flow)
            }
            if (currentState.shouldShowDeleteSuccessSnackBar) {
               showSnackBar(
                    message = MyListsSnackBarMessage.SavedListDeletedSuccessfully,
                    isSuccess = true,
               )
                updateState { it.copy(shouldShowDeleteSuccessSnackBar = false) }
            } else {
                hideSnackBar()
            }
        }

        private fun onGetSavedListsLoadingChanged(isLoading: Boolean) {
            updateState {
                it.copy(
                    isLoading = isLoading,
                )
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

        override fun onAddListFabClick() {
            updateState {
                it.copy(
                    addListBottomSheetState =
                        AddListBottomSheetState(
                            isVisible = true,
                        ),
                )
            }
        }

        override fun onLoginClick() {
            sendEffect(MyListsScreenEffect.NavigateToLogin)
        }

        override fun onAddedListNameChange(name: String) {
            updateState {
                it.copy(
                    addListBottomSheetState =
                        it.addListBottomSheetState.copy(
                            listName = name,
                        ),
                )
            }
        }

        override fun onAddListBottomSheetDismiss() {
            updateState {
                it.copy(
                    addListBottomSheetState =
                        it.addListBottomSheetState.copy(
                            isVisible = false,
                        ),
                )
            }
        }

        override fun onAddListBottomSheetAddClick() {
            val listName = uiState.value.addListBottomSheetState.listName
            tryToExecute(
                callee = { createSavedListUseCase(listName) },
                dispatcher = defaultDispatcher,
                onStart = ::onAddListStart,
                onSuccess = { onAddListSuccess() },
                onFinally = ::onAddListFinally,
            )
        }

        private fun onAddListSuccess() {
            updateState {
                it.copy(
                    addListBottomSheetState =
                        it.addListBottomSheetState.copy(
                            isVisible = false,
                        ),
                )
            }
            showAddSuccessSnackBar()
            getSavedLists()
        }

        private fun showAddSuccessSnackBar() {
            showSnackBar(
                message = MyListsSnackBarMessage.SavedListCreatedSuccessfully,
                isSuccess = true,
            )
        }

        private fun onAddListFinally() {
            updateState {
                it.copy(
                    addListBottomSheetState =
                        it.addListBottomSheetState.copy(
                            isLoading = false,
                        ),
                )
            }
        }

        private fun onAddListStart() {
            updateState {
                it.copy(
                    addListBottomSheetState = it.addListBottomSheetState.copy(isLoading = true),
                )
            }
        }

        override fun onListClick(listId: Long) {
            sendEffect(MyListsScreenEffect.NavigateToViewSavedDetails(listId))
        }

        override fun onSnackBarActionLabelClick() {
            hideSnackBar()
            getSavedLists()
        }

        override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage = BaseSnackBarMessage.UnknownError

        companion object {
            private const val PAGES_SIZE = 20
        }
    }
