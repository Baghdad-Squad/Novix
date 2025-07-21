package com.baghdad.viewmodel.login

interface LoginInteractionListener {
    fun onLoginClicked(userName: String, password: String)
    fun onRegisterClicked()
    fun onForgotPasswordClicked()
    fun onNavigateBackClicked()
}