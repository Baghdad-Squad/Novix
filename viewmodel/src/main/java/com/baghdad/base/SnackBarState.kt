package com.baghdad.base

data class SnackBarState(
    val message: String? = null,
    val isSuccess: Boolean = false,
    val isVisible: Boolean = false
)