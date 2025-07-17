package com.baghdad.viewmodel.review

import com.baghdad.viewmodel.base.BaseUiEffect

sealed class ReviewScreenEffect : BaseUiEffect {
    object NavigateBack : ReviewScreenEffect()
}