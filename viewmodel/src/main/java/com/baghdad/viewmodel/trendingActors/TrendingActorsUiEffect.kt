package com.baghdad.viewmodel.trendingActors

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class TrendingActorsUiEffect :BaseUiEffect{
    data object OnBackClick: TrendingActorsUiEffect()
    data class NavigateToActorsDetails(val actorId: Long) : TrendingActorsUiEffect()
}