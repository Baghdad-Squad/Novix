package com.baghdad.domain.usecase.genre

import com.baghdad.entity.media.Genre

object GenreMock {
    val GENRE_ID = 1L
    val GENRE = Genre(id = GENRE_ID, name = "Sample Genre")
    val GENRES = listOf(GENRE, GENRE.copy(id = 2, name = "Another Genre"))

}