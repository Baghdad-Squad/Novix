package com.baghdad.domain.testHelper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.baghdad.entity.person.CastMember
import kotlinx.datetime.LocalDate

fun getTestMovie(
    id: Long = 1L,
    title: String = "Test Movie",
    genres: List<Genre> = emptyList(),
    imdbRating: Double = 7.5,
    userRating: Double? = null,
    releaseDate: LocalDate = LocalDate(2020, 1, 1),
    overview: String = "Overview of Test Movie",
    cast: List<CastMember> = emptyList(),
    posterPictureURL: String = "http://example.com/poster.jpg",
    backdropPicturesURLs: List<String> = listOf("http://example.com/backdrop.jpg"),
    runtimeMinutes: Int = 120
) = Movie(
    id = id,
    title = title,
    genres = genres,
    imdbRating = imdbRating,
    userRating = userRating,
    releaseDate = releaseDate,
    overview = overview,
    cast = cast,
    posterPictureURL = posterPictureURL,
    backdropPicturesURLs = backdropPicturesURLs,
    runtimeMinutes = runtimeMinutes
)

fun getTestMovies(
    count: Int
): List<Movie> = (1..count).map { index ->
    getTestMovie(
        id = index.toLong(),
        title = "Test Movie $index",
        releaseDate = LocalDate(2020, 1, index % 28 + 1),
        posterPictureURL = "http://example.com/poster$index.jpg",
        backdropPicturesURLs = listOf("http://example.com/backdrop$index.jpg"),
    )
}