package com.baghdad.design_system.shared

data class Selectable<T>(
    val value:T,
    val isSelected:Boolean = false
)
