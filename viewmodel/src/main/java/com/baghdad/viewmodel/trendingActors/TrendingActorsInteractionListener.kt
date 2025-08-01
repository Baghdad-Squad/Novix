package com.baghdad.viewmodel.trendingActors

interface TrendingActorsInteractionListener{
    fun onBackClick()
    fun onTrendingActorClick(actorId: Long)
    fun onSnackBarActionLabelClick()
}