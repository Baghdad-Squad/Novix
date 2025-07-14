package com.baghdad.viewmodel.gallery

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.base.SnackBarState

data class GalleryScreenState(
    val images: List<String> = emptyList(),
    override val isLoading: Boolean = false
) : BaseUiState

