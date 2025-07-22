package com.baghdad.viewmodel.errorStates

sealed interface BaseSnackBarMessage {
    data object NetworkError : SearchScreenBaseSnackBarMessages
    data object UnAuthorizedError : SearchScreenBaseSnackBarMessages
    data object UnknownError : SearchScreenBaseSnackBarMessages
    data object DataBaseError : SearchScreenBaseSnackBarMessages
    data object DefaultMessage : SearchScreenBaseSnackBarMessages
    data object LoginSuccessfully : LoginSnackBarMessage()
    data object InvalidCredential : LoginSnackBarMessage()
}
