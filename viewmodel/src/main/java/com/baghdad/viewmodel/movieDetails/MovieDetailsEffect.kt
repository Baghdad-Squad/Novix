package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface MovieDetailsEffect : BaseUiEffect{

    data class NavigateToCategory(
        val categoryName: String
    ): MovieDetailsEffect

    data class NavigateToReviewDetails(
        val id: Long
    ) : MovieDetailsEffect

    data class NavigateToActorDetails(
        val id: String
    ): MovieDetailsEffect

    data class NavigateToMovie(
        val id: String
    ): MovieDetailsEffect

}