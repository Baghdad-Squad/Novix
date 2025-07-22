package com.baghdad.viewmodel.login

interface LoginInteractionListener {
    fun onLoginClicked()
    fun onRegisterClicked()
    fun onForgotPasswordClicked()
    fun onNavigateBackClicked()
    fun onPasswordValueChange(value: String)
    fun onUserNameValueChange(value: String)
    fun togglePasswordVisibility()
}