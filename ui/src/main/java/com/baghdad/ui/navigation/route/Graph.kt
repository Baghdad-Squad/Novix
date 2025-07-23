package com.baghdad.ui.navigation.route

import com.baghdad.viewmodel.review.ContentType
import kotlinx.serialization.Serializable

sealed interface Graph : Route {
    @Serializable
    data object OnBoardingGraph : Graph

    @Serializable
    data object AuthenticationGraph : Graph

    @Serializable
    data object HomeGraph : Graph

    @Serializable
    data object SearchGraph : Graph

    @Serializable
    data object CategoriesGraph : Graph

    @Serializable
    data object MyListsGraph : Graph

    @Serializable
    data object MyAccountGraph : Graph

    @Serializable
    data class ActorDetailsGraph(val actorId: Long) : Graph

    @Serializable
    data class MovieDetailsGraph(val movieId: Long) : Graph

    @Serializable
    data class TvShowDetailsGraph(val tvShowId: Long) : Graph

    @Serializable
    data class ReviewsGraph(val mediaId: Long, val mediaType: ContentType) : Graph

}