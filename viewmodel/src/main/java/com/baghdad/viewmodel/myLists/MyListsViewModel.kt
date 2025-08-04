package com.baghdad.viewmodel.myLists

import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.savedList.CreateSavedListUseCase
import com.baghdad.domain.usecase.savedList.GetSavedListsUseCase
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class MyListsViewModel(
    private val getSavedListsUseCase: GetSavedListsUseCase,
    private val createSavedListUseCase: CreateSavedListUseCase,
    private val isUserLoggedInUseCase: IsLoggedInUseCase,
) : BaseViewModel<MyListsScreenState, MyListsScreenEffect>(MyListsScreenState()),
    MyListsInteractionListener {
    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
//        TODO("Not yet implemented")
        // Inside on success should call getSavedListsUseCase
    }

    private fun getSavedLists() {
        // TODO("Not yet implemented")
    }

    override fun onAddListFabClick() {
        TODO("Not yet implemented")
    }

    override fun onLoginClick() {
        TODO("Not yet implemented")
    }

    override fun onAddedListNameChange(name: String) {
        TODO("Not yet implemented")
    }

    override fun onAddListBottomSheetDismiss() {
        TODO("Not yet implemented")
    }

    override fun onAddListBottomSheetAddClick() {
        TODO("Not yet implemented")
    }

    override fun onListClick(listId: Long) {
        TODO("Not yet implemented")
    }

    override fun onSnackBarActionLabelClick() {
        TODO("Not yet implemented")
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }
}
