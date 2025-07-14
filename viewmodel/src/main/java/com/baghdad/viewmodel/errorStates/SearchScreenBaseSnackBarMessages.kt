package com.baghdad.viewmodel.errorStates

sealed interface SearchScreenBaseSnackBarMessages : BaseSnackBarMessage {
    data object SavedItemSuccessfully : SearchScreenBaseSnackBarMessages
    data object RemovedItemSuccessfully : SearchScreenBaseSnackBarMessages
}

