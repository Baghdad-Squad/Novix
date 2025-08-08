package com.baghdad.viewmodel.login

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class LoginUiEffect : BaseUiEffect {
    object NavigateBack : LoginUiEffect()
    object NavigateToRegister : LoginUiEffect()
    object NavigateToForgotPassword : LoginUiEffect()

    object RecreateActivity : LoginUiEffect()
}