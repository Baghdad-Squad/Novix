package com.baghdad.viewmodel.share

data class ListUiState(
    val id: Long,
    val name: String,
    val itemsCount: Int,
    val isSelected: Boolean
)