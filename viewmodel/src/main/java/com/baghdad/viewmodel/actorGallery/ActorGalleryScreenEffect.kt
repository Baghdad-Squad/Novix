package com.baghdad.viewmodel.actorGallery

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class ActorGalleryScreenEffect: BaseUiEffect {
   data object OnBackClick: ActorGalleryScreenEffect()

}