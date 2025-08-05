package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseUiState

data class ProfileScreenUIState(
    val isLoading: Boolean = false,
    val isLogin: Boolean = false,
    val userInfo: User = User(),
    val userSettings: UserSettings = UserSettings()
) : BaseUiState {
    data class User(
        val userName: String = "",
        val imageUrl: String = "",
    )

    data class UserSettings(
        val appearance: String = "",
        val language: String = "",
    )
}