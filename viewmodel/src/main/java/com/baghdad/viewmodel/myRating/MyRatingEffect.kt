package com.baghdad.viewmodel.myRating

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface MyRatingEffect : BaseUiEffect {
    data object NavigateBack : MyRatingEffect
    data class NavigateToMovieDetails(
        val movieId: Long
    ) : MyRatingEffect

    data class NavigateToTvShowDetails(
        val tvShowId: Long
    ) : MyRatingEffect
}