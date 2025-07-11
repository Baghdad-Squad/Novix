package com.baghdad.domain.testHelper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember

fun getTestTvShow(
    id: Long = 1L,
    title: String = "Test TV Show",
    genres: List<Genre> = emptyList(),
    imdbRating: Double = 8.2,
    userRating: Double? = null,
    releaseDate: kotlinx.datetime.LocalDate = kotlinx.datetime.LocalDate(2020, 1, 1),
    overview: String = "Overview of Test TV Show",
    cast: List<CastMember> = emptyList(),
    posterPictureURL: String = "http://example.com/poster.jpg",
    backdropPicturesURLs: List<String> = listOf("http://example.com/backdrop.jpg"),
    numberOfSeasons: Int = 3
) = TvShow(
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
    numberOfSeasons = numberOfSeasons
)

fun getTestTvShows(
    count: Int
): List<TvShow> = (1..count).map { index ->
    getTestTvShow(
        id = index.toLong(),
        title = "Test TV Show $index",
        releaseDate = kotlinx.datetime.LocalDate(2020, 1, index % 28 + 1),
        posterPictureURL = "http://example.com/poster$index.jpg",
        backdropPicturesURLs = listOf("http://example.com/backdrop$index.jpg"),
    )
}