package com.baghdad.viewmodel.errorStates

sealed class LoginSnackBarMessage : BaseSnackBarMessage {
    data object LoginSuccessfully : LoginSnackBarMessage()
    data object InvalidCredential : LoginSnackBarMessage()
    data object NoInternetConnection : LoginSnackBarMessage()
    data object LoginOutSuccessfully : LoginSnackBarMessage()
}