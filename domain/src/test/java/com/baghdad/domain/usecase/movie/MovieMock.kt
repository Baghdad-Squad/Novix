package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.domain.usecase.genre.GenreMock
import com.baghdad.entity.media.Movie
import kotlinx.datetime.LocalDate

object MovieMock {
    val LIST_ID = 1L
    val PAGE = 1
    val PAGE_SIZE = 20
    val MOVIE_ID = 1L

    val MOVIE = Movie(
        id = MOVIE_ID,
        title = "The Godfather",
        genres = GenreMock.GENRES,
        userRating = 8.5,
        averageRating = 10.0,
        releaseDate = LocalDate(2002, 2, 22),
        overview = "The aging patriarch of an organized crime dynasty transfers control.",
        posterImageURL = "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFry",
        trailerURL = "https://www.youtube.com/watch?v=sY1S34973zA",
        runtimeMinutes = 175
    )

    val SAVED_MOVIE = SavedMovie(
        movie = MOVIE,
        isSaved = false,
        listId = LIST_ID
    )

    val SAVED_MOVIES = listOf(
        SAVED_MOVIE,
        SAVED_MOVIE.copy(movie = MOVIE.copy(id = 2, genres = GenreMock.GENRES)),
        SAVED_MOVIE.copy(movie = MOVIE.copy(id = 3, genres = GenreMock.GENRES))
    )

    val SAVED_MOVIES_PAGED_RESULT = PagedResult(
        data = SAVED_MOVIES,
        nextPage = 2,
        prevPage = 1
    )
}