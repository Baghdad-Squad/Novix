package com.baghdad.viewmodel.shared

data class ListUiState(
    val id: Long,
    val name: String,
    val isSelected: Boolean,
    val itemsCount: Int? = null,
    val description: String? = null,
)