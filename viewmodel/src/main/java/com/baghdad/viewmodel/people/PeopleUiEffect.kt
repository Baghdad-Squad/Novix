package com.baghdad.viewmodel.people

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class PeopleUiEffect :BaseUiEffect{
    data object OnBackClick: PeopleUiEffect()
    data class NavigateToActorDetails(val actorId: Long) : PeopleUiEffect()
}