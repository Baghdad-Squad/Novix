package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface MovieDetailsEffect : BaseUiEffect{

    data class NavigateToCategory(
        val id: Long
    ): MovieDetailsEffect

    data class NavigateToReviewDetails(
        val id: Long
    ) : MovieDetailsEffect

    data class NavigateToActorDetails(
        val id: Long
    ): MovieDetailsEffect

    data class NavigateToMovie(
        val id: Long
    ): MovieDetailsEffect
    data class OpenYoutubeLink(
        val youtubeLink: String
    ) : MovieDetailsEffect
    data object NavigateBack: MovieDetailsEffect
}