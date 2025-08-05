package com.baghdad.viewmodel.shared

data class ListUiState(
    val id: Long,
    val name: String,
    val itemsCount: Int,
    val isSelected: Boolean
)