package com.baghdad.viewmodel.categoryMovies

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface CategoryMoviesEffect : BaseUiEffect {
    object NavigateBack : CategoryMoviesEffect
    data class NavigateToMovieDetails(val movieId: Long) : CategoryMoviesEffect
}