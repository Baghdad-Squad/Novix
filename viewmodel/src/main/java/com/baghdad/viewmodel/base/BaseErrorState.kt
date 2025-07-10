package com.baghdad.viewmodel.base

sealed interface BaseErrorState {
    data object NoInternet : BaseErrorState
    data object UnAuthorized : BaseErrorState
    data object ServerError : BaseErrorState
}