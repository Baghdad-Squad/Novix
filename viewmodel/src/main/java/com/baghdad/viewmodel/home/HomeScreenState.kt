package com.baghdad.viewmodel.home

data class HomeScreenState(
    val popularItems: List<PopularItemUiState> = emptyList(),
) {
    data class PopularItemUiState(
        val name: String,
        val rating: Double,
        val imageUrl: String,
        val isSaved: Boolean,
        val onClick: () -> Unit,
        val onSavedClick: () -> Unit
    )
}