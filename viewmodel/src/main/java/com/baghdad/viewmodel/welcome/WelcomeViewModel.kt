package com.baghdad.viewmodel.welcome

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<WelcomeStateUiState, WelcomeEffect>(WelcomeStateUiState()),
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