package com.baghdad.domain.usecase.appConfigurations

import com.baghdad.domain.model.profile.ContentRestrictionTypes
import com.baghdad.domain.repository.AppConfigurationsRepository
import jakarta.inject.Inject

class SetContentRestrictionUseCase @Inject constructor(
    private val appConfigurationsRepository: AppConfigurationsRepository
) {
    suspend operator fun invoke(contentRestriction: ContentRestrictionTypes) {
        appConfigurationsRepository.setContentRestriction(contentRestriction)
    }
}