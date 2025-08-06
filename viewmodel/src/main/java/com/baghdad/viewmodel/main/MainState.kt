package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiState

data class MainState(
    val isFirstTimeUser: Boolean? = null,
    val isLoggedIn: Boolean? = null,
    val isLoading: Boolean = true
) : BaseUiState