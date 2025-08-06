package com.baghdad.viewmodel.onBoarding


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.baghdad.viewmodel.base.BaseUiState

data class OnBoardingState(
    val currentPage: Int = 0,
): BaseUiState



data class OnBoardingInfo(
    @DrawableRes val imageIndex: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
)
