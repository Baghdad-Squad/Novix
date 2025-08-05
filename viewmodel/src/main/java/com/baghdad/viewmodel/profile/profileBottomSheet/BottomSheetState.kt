package com.baghdad.viewmodel.profile.profileBottomSheet

import com.baghdad.domain.model.AppAppearanceMode
import com.baghdad.viewmodel.base.BaseUiState

data class BottomSheetState(
    val isVisible: Boolean = false,
    val currentTheme: AppAppearanceMode = AppAppearanceMode.DARK,
    val currentLanguage: String = "en",
    ): BaseUiState