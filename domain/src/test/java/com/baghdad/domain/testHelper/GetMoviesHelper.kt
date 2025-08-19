package com.baghdad.domain.testHelper

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import kotlinx.datetime.LocalDate

fun getSampleMovie(
    id: Long = 1L,
    title: String = "Test Movie",
    genres: List<Genre> = emptyList(),
    imdbRating: Double = 7.5,
    userRating: Double? = null,
    releaseDate: LocalDate = LocalDate(2020, 1, 1),
    overview: String = "Overview of Test Movie",
    posterPictureURL: String = "http://example.com/poster.jpg",
    runtimeMinutes: Int = 120,
    trailerURL: String = "http://example.com/trailer.mp4"
) = Movie(
    id = id,
    title = title,
    genres = genres,
    averageRating = imdbRating,
    userRating = userRating,
    releaseDate = releaseDate,
    overview = overview,
    posterImageURL = posterPictureURL,
    runtimeMinutes = runtimeMinutes,
    trailerURL = trailerURL,
)

fun getSampleSavedMovie(
    movie: Movie = getSampleMovie(),
    isSaved: Boolean = true,
    listId: Long = 1L
) = SavedMovie(
    movie = movie,
    isSaved = isSaved,
    listId = listId
)


fun getMinimalMovie(
    id: Long = 2L,
    title: String = "Minimal Movie",
    genres: List<Genre> = emptyList(),
    imdbRating: Double = 0.0,
    userRating: Double? = null,
    releaseDate: LocalDate = LocalDate(2021, 1, 1),
    overview: String = "",
    posterImageURL: String = "",
    runtimeMinutes: Int = 0,
    trailerURL: String = ""
) = Movie(
    id = id,
    title = title,
    genres = genres,
    averageRating = imdbRating,
    userRating = null,
    releaseDate = LocalDate(2021, 1, 1),
    overview = overview,
    posterImageURL = posterImageURL,
    trailerURL = trailerURL,
    runtimeMinutes = runtimeMinutes
)


fun getMinimalSavedMovie(
    movie: Movie = getMinimalMovie(),
    isSaved: Boolean = false,
    listId: Long = 1L
) = SavedMovie(
    movie = getMinimalMovie(),
    isSaved = isSaved,
    listId = listId
)

