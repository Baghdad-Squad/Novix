package com.baghdad.viewmodel.savedListDetails

import com.baghdad.viewmodel.base.BaseUiEffect

sealed interface SavedListDetailsEffect : BaseUiEffect{
    data object NavigateBack: SavedListDetailsEffect
}