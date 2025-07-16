package com.baghdad.viewmodel.actorGallery

import com.baghdad.viewmodel.base.BaseUiState

data class ActorGalleryScreenState(
    val images: List<String> = emptyList(),
    override val isLoading: Boolean = false
) : BaseUiState

