package com.baghdad.domain.testHelper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow

fun getSampleTvShow(
    id: Long = 1L,
    title: String = "Test TV Show",
    genres: List<Genre> = emptyList(),
    imdbRating: Double = 8.2,
    userRating: Int? = null,
    releaseDate: kotlinx.datetime.LocalDate = kotlinx.datetime.LocalDate(2020, 1, 1),
    overview: String = "Overview of Test TV Show",
    posterPictureURL: String = "http://example.com/poster.jpg",
    numberOfSeasons: Int = 3,
    headerImagesURLs: List<String> = listOf("http://example.com/header1.jpg", "http://example.com/header2.jpg"),
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
    headerImagesURLs = headerImagesURLs,
    trailerURL = ""
)

fun getMinimalTvShow(
    id: Long = 3L,
    title: String = "Minimal Show",
    overview: String = "",
    posterImageURL: String = "",
    genres: List<Genre> = emptyList(),
    averageRating: Double = 0.0,
    userRating: Int? = null,
    releaseDate: kotlinx.datetime.LocalDate = kotlinx.datetime.LocalDate(2021, 1, 1),
    trailerURL: String = "",
    headerImagesURLs: List<String> = emptyList(),
    numberOfSeasons: Int = 0
) = TvShow(
    id = id,
    title = title,
    overview = overview,
    posterImageURL = posterImageURL,
    genres = genres,
    averageRating = averageRating,
    userRating = userRating,
    releaseDate = releaseDate,
    trailerURL = trailerURL,
    headerImagesURLs = headerImagesURLs,
    numberOfSeasons = numberOfSeasons
)
