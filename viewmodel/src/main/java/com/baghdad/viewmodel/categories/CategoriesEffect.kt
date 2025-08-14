package com.baghdad.viewmodel.categories

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface CategoriesEffect: BaseUiEffect {
    data class NavigateToCategoryMovies(val id: Long) : CategoriesEffect
    data class NavigateToCategoryTVShows(val id: Long) : CategoriesEffect

}