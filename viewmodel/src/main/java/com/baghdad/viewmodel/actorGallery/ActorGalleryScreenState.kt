package com.baghdad.viewmodel.actorGallery

import com.baghdad.viewmodel.base.BaseUiState

data class ActorGalleryScreenState(
    val images: List<String> = emptyList(),
    val selectedImageUrl : String = "",
    val isLoading: Boolean = false
) : BaseUiState

