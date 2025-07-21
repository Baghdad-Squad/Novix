package com.baghdad.viewmodel.login

import com.baghdad.viewmodel.base.BaseUiState

data class LoginUiState(
    override val isLoading: Boolean = false,
    val userName: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isAnyFieldEmpty: Boolean = true
) : BaseUiState