package com.baghdad.repository.model

data class GenreDto(
    val id: Long,
    val name: String,
    val type: GenreType
) {
    enum class GenreType {
        MOVIE,
        TV_SHOW
    }
}