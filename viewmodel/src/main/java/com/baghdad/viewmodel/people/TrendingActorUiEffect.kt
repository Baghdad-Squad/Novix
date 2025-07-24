package com.baghdad.viewmodel.people

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TrendingActorUiEffect :BaseUiEffect{
    data object OnBackClick: TrendingActorUiEffect()
    data class NavigateToActorDetails(val actorId: Long) : TrendingActorUiEffect()
}