package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseUiEffect


interface ProfileEffect : BaseUiEffect {
    data object NavigateBack : ProfileEffect
    data object NavigateToMyRatings : ProfileEffect
    data object NavigateToWatchingHistory : ProfileEffect
    data object NavigateToLogin : ProfileEffect
    data object NavigateToChangePassword : ProfileEffect

}