package com.baghdad.base

open class BaseUiState(
    val isLoading: Boolean = false,
    val snackBarState: SnackBarState = SnackBarState(),
    val baseErrorState: BaseErrorState? = null
)

