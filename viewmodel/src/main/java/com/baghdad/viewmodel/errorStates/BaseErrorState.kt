package com.baghdad.viewmodel.errorStates

sealed interface BaseErrorState {
    data object NoInternet : BaseErrorState
    data object UnAuthorized : BaseErrorState
    data object ServerError : BaseErrorState
}