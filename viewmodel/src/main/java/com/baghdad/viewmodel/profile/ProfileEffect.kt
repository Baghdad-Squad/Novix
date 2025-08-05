package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class ProfileEffect : BaseUiEffect {
    data object NavigateToLogin : ProfileEffect()
}