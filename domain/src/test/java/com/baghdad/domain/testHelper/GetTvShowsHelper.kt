package com.baghdad.domain.testHelper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow

fun getTestTvShow(
    id: Long = 1L,
    title: String = "Test TV Show",
    genres: List<Genre> = emptyList(),
    imdbRating: Double = 8.2,
    userRating: Double? = null,
    releaseDate: kotlinx.datetime.LocalDate = kotlinx.datetime.LocalDate(2020, 1, 1),
    overview: String = "Overview of Test TV Show",
    posterPictureURL: String = "http://example.com/poster.jpg",
    numberOfSeasons: Int = 3
) = TvShow(
    id = id,
    title = title,
    genres = genres,
    averageRating = imdbRating,
    userRating = userRating,
    releaseDate = releaseDate,
    overview = overview,
    posterImageURL = posterPictureURL,
    numberOfSeasons = numberOfSeasons,
    headerPictures = emptyList()
)

fun getTestTvShows(
    size: Int
): List<TvShow> = List(size) { index ->
    getTestTvShow(
        id = index.toLong(),
        title = "Test TV Show $index",
        releaseDate = kotlinx.datetime.LocalDate(2020, 1, index % 28 + 1),
        posterPictureURL = "http://example.com/poster$index.jpg",
    )
}