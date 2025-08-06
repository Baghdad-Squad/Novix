package com.baghdad.viewmodel.onBoarding

interface OnBoardingInteractionListener {
    fun onNextButtonClick(sizeOfBoardingInfo: Int)
    fun onBackButtonClick()
    fun onSkipButtonClick()
}