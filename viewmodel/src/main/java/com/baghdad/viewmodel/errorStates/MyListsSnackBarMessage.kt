package com.baghdad.viewmodel.errorStates

sealed interface MyListsSnackBarMessage : BaseSnackBarMessage {
    data object SavedListCreatedSuccessfully : MyListsSnackBarMessage
    data object SavedListDeletedSuccessfully : MyListsSnackBarMessage
}
