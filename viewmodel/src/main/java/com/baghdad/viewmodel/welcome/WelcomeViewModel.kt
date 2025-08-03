package com.baghdad.viewmodel.welcome

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class WelcomeViewModel() : BaseViewModel<WelcomeStateUiState, WelcomeEffect>(WelcomeStateUiState()),
    WelcomeInteractionListener {

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        return BaseSnackBarMessage.DefaultMessage
    }

    override fun onClickLogin() {
        sendEffect(WelcomeEffect.NavigateToLogin)
    }

    override fun onClickContinueAsGuest() {
        sendEffect(WelcomeEffect.NavigateToContinueAsGuest)
    }
}