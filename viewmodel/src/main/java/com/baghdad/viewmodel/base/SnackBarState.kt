package com.baghdad.viewmodel.base

import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

data class SnackBarState(
    val message: BaseSnackBarMessage = BaseSnackBarMessage.DefaultMessage,
    val isSuccess: Boolean = false,
    val isVisible: Boolean = false
)