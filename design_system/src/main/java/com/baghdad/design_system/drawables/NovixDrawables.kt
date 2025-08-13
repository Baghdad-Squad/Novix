package com.baghdad.design_system.drawables

import androidx.annotation.DrawableRes
import androidx.compose.runtime.staticCompositionLocalOf

data class NovixDrawables(
    @DrawableRes val imagePlaceholder: Int,
    @DrawableRes val noSearchResult: Int,
    @DrawableRes val startExplore: Int,
    @DrawableRes val personAvatar: Int,
    @DrawableRes val emptyList: Int,
    @DrawableRes val emptySavedLists: Int,
    @DrawableRes val emptyReviews: Int,
    @DrawableRes val savedListsNoLogin: Int,
    @DrawableRes val trashCan: Int,
    @DrawableRes val welcomeBackground: Int,
)

val localNovixDrawables = staticCompositionLocalOf { darkThemeDrawables }
