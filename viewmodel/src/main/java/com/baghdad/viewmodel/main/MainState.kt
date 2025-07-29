package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiState

data class MainState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false
) : BaseUiState