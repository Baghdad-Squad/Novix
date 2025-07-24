package com.baghdad.viewmodel.people

import com.baghdad.entity.person.Actor

fun Actor.toTrendingActorsUi() = TrendingActorUiState.TrendingActor(
    id = this.id,
    profilePictureURL = this.profilePictureURL,
    name = this.name

)
