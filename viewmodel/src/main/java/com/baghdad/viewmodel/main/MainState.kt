package com.baghdad.viewmodel.main

import com.baghdad.viewmodel.base.BaseUiState

data class MainState(
    val isLoggedIn: Boolean? = null,
    override val isLoading: Boolean = true
) : BaseUiState