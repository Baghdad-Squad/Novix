package com.baghdad.viewmodel.base

import com.baghdad.viewmodel.errorStates.BaseErrorState

interface BaseUiState {
    val isLoading: Boolean
    val snackBarState: SnackBarState
    val baseErrorState: BaseErrorState?
}



