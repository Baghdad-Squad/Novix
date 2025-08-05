package com.baghdad.viewmodel.errorStates

sealed interface BaseSnackBarMessage {
    data object LoginSuccessfully : LoginSnackBarMessage()
    data object InvalidCredential : LoginSnackBarMessage()
    data object NetworkError : SearchSnackBarMessage
    data object UnAuthorizedError : SearchSnackBarMessage
    data object UnknownError : SearchSnackBarMessage
    data object DataBaseError : SearchSnackBarMessage
    data object DefaultMessage : SearchSnackBarMessage
    data object NoInternetException : LoginSnackBarMessage()
    data object LoginOutSuccessfully : LoginSnackBarMessage()
    data object SavedItemSuccessfully : DetailsScreensSnackBarMessage
    data object RemovedItemSuccessfully : DetailsScreensSnackBarMessage
    data object ItemRateSuccessfully : DetailsScreensSnackBarMessage
}
