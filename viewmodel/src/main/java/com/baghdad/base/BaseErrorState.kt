package com.baghdad.base

sealed interface BaseErrorState {
    data object NoInternet : BaseErrorState
    data object UnAuthorized : BaseErrorState
    data object ServerError : BaseErrorState
}