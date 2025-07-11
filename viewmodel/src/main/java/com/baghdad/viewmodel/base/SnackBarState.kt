package com.baghdad.viewmodel.base

data class SnackBarState(
    val message: String? = null,
    val isSuccess: Boolean = false,
    val isVisible: Boolean = false
)