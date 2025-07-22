package com.baghdad.ui.navigation.graph.people

sealed interface PeopleNavEvent{
    data object NavigateBack : PeopleNavEvent
    data class NavigateToActorDetails(val actorId: Long) : PeopleNavEvent

}