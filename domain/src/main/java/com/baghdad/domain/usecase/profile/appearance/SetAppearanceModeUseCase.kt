package com.baghdad.domain.usecase.profile.appearance

import com.baghdad.domain.model.AppAppearanceMode
import com.baghdad.domain.repository.AppearanceRepository
import javax.inject.Inject

class SetAppearanceModeUseCase@Inject constructor(
    private val repository: AppearanceRepository
) {
    suspend operator fun invoke(appearance: AppAppearanceMode) {
        repository.setAppearanceMode(appearance)
    }
}
