package com.baghdad.viewmodel.base

open class BaseUiState(
    val isLoading: Boolean = false,
    val snackBarState: SnackBarState = SnackBarState(),
    val baseErrorState: BaseErrorState? = null
)

