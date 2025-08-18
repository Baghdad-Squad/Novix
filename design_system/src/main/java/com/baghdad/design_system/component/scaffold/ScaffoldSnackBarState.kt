package com.baghdad.design_system.component.scaffold

import com.baghdad.design_system.component.SnackBarPosition

data class ScaffoldSnackBarState(
    val isVisible: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false,
    val actionLabel: String? = null,
    val position: SnackBarPosition = SnackBarPosition.BOTTOM,
)
