package com.baghdad.viewmodel.onBoarding

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface OnBoardingEffect : BaseUiEffect{

    data object NavigateToWelcomeToNovix : OnBoardingEffect
}