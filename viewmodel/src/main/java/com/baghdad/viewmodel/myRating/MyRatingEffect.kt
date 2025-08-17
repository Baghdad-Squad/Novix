package com.baghdad.viewmodel.myRating

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface MyRatingEffect : BaseUiEffect {

    data class NavigateToMovieDetails(
        val movieId: Long
    ) : MyRatingEffect

    data class NavigateToTvShowDetails(
        val tvShowId: Long
    ) : MyRatingEffect

    data object NavigateBack : MyRatingEffect

}
