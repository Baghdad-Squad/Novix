package com.baghdad.viewmodel.trendingActors

import com.baghdad.entity.person.Actor

fun Actor.toTrendingActorsUi() = TrendingActorsUiState.TrendingActor(
    id = this.id,
    profilePictureURL = this.profilePictureURL,
    name = this.name
)
