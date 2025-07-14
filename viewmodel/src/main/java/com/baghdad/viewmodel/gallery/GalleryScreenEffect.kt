package com.baghdad.viewmodel.gallery

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class GalleryScreenEffect: BaseUiEffect {
   object OnBackClick: GalleryScreenEffect()

}