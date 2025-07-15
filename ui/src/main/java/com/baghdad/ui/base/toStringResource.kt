package com.baghdad.ui.base

import androidx.annotation.StringRes
import com.baghdad.ui.R
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

@StringRes
fun BaseSnackBarMessage.toStringResource(): Int {
    return when (this) {
        BaseSnackBarMessage.NetworkError -> R.string.snackbar_network_error
        BaseSnackBarMessage.DataBaseError -> R.string.snackbar_database_error
        BaseSnackBarMessage.UnAuthorizedError -> R.string.snackbar_unauthorized_error
        BaseSnackBarMessage.UnknownError -> R.string.snackbar_unknown_error
        BaseSnackBarMessage.DefaultMessage -> R.string.empty_string
        else -> R.string.empty_string
    }
}