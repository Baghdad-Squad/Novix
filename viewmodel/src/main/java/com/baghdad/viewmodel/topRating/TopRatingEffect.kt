package com.baghdad.viewmodel.topRating

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface TopRatingEffect : BaseUiEffect {
    data class NavigateToMovieDetails(val movieId: Long): TopRatingEffect
    data object NavigateBack: TopRatingEffect
}
