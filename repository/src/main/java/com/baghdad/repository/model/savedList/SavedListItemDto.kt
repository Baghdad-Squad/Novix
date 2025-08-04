package com.baghdad.repository.model.savedList

data class SavedListItemDto(
    val id: Long,
    val type: Type,
    val title: String,
    val posterUrl: String,
) {
    enum class Type {
        MOVIE,
        TV_SHOW,
    }
}
