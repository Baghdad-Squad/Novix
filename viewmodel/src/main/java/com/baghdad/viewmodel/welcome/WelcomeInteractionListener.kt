package com.baghdad.viewmodel.welcome

sealed interface WelcomeInteractionListener {
    fun onClickLogin()
    fun onClickContinueAsGuest()
}
