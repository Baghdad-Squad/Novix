package com.baghdad.ui.navigation.graph.reviews

sealed interface ReviewsNavEvent {
    data object NavigateBack : ReviewsNavEvent
}