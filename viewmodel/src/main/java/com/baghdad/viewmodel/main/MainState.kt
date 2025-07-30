package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiState

data class MainState(
    val isLoggedIn: Boolean? = null,
    val isLoading: Boolean = true
) : BaseUiState