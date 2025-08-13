package com.baghdad.viewmodel.login

interface LoginInteractionListener {
    fun onLoginClick()
    fun onRegisterClick()
    fun onForgotPasswordClick()
    fun onBackClick()
    fun onPasswordValueChange(value: String)
    fun onUserNameValueChange(value: String)
    fun togglePasswordVisibility()
}