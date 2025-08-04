package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseUiState

data class ProfileScreenUIState(
    val isLogin: Boolean = false,
    val userName: String,
    val imageUrl: String,
    val appearance: String,
    val language: String
) : BaseUiState