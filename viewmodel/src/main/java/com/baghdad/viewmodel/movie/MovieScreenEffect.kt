package com.baghdad.viewmodel.movie

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class MovieScreenEffect : BaseUiEffect {
    data class NavigateToDetails(val movieId: Long) : MovieScreenEffect()
    object NavigateBack : MovieScreenEffect()
}