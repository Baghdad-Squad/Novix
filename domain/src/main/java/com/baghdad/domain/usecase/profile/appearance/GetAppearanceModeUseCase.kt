package com.baghdad.domain.usecase.profile.appearance

import com.baghdad.domain.model.AppAppearanceMode
import com.baghdad.domain.repository.AppearanceRepository
import javax.inject.Inject

class GetAppearanceModeUseCase@Inject constructor(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(): AppAppearanceMode {
        return repository.getAppearanceMode()
    }
}