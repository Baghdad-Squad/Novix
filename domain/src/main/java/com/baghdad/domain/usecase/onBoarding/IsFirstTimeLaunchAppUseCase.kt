package com.baghdad.domain.usecase.onBoarding

import com.baghdad.domain.repository.OnBoardingRepository
import javax.inject.Inject

class IsFirstTimeLaunchAppUseCase @Inject constructor(
    private val repository: OnBoardingRepository
) {
    suspend operator fun invoke() = repository.isFirstTimeUser()
}