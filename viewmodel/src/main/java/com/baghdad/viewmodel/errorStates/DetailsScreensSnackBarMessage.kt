package com.baghdad.viewmodel.errorStates

sealed interface DetailsScreensSnackBarMessage: BaseSnackBarMessage {
    data object SavedItemSuccessfully : DetailsScreensSnackBarMessage
    data object RemovedItemSuccessfully : DetailsScreensSnackBarMessage
}