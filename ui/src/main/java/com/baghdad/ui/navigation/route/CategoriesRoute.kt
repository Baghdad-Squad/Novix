package com.baghdad.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface CategoriesRoute : Route {
    @Serializable
    data object CategoriesScreen : CategoriesRoute

    @Serializable
    data class CategoryMoviesScreen(val categoryId: Long) : CategoriesRoute

    @Serializable
    data class CategoryTvShowsScreen(val categoryId: Long) : CategoriesRoute
}