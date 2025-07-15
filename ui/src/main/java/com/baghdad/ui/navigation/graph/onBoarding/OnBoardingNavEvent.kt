package com.baghdad.ui.navigation.graph.onBoarding

sealed interface OnBoardingNavEvent {
    data object NavigateToHome : OnBoardingNavEvent
    data object NavigateToLogin : OnBoardingNavEvent
    data object NavigateToWelcome : OnBoardingNavEvent
}