package com.baghdad.viewmodel.errorStates

sealed interface SearchSnackBarMessage : BaseSnackBarMessage {
    data object SavedItemSuccessfully : SearchSnackBarMessage
    data object RemovedItemSuccessfully : SearchSnackBarMessage
}

