package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.OnBoardingRepository
import javax.inject.Inject

class SetFirstTimeLaunchAppUseCase @Inject constructor(
    private val repository: OnBoardingRepository
) {
    suspend operator fun invoke(firstTime: Boolean){
        repository.setFirstTimeUser(firstTime = firstTime)
    }
}