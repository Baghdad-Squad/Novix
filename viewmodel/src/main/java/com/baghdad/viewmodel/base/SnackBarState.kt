package com.baghdad.viewmodel.base

import androidx.annotation.StringRes
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

data class SnackBarState(
    val message: BaseSnackBarMessage = BaseSnackBarMessage.DefaultMessage,
    @StringRes
    val actionLabelRes: Int? = null,
    val isSuccess: Boolean = false,
    val isVisible: Boolean = false
)