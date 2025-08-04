package com.baghdad.domain.model.savedList

data class SavedListItem(
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
