package com.baghdad.domain.testHelper

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre

fun getSampleEpisode(
    id: Long = 123L,
    title: String = "The Heirs of the Dragon",
    episodeNumber: Int = 1,
    rating: Double = 8.5,
    duration: String = "1h 5m",
    releasedDate: kotlinx.datetime.LocalDate = kotlinx.datetime.LocalDate(2022, 8, 21),
    trailerUrl: String = "https://www.youtube.com/watch?v=example",
    currentSeason: Int = 1,
    overview: String = "King Viserys hosts a tournament to celebrate the birth of his second child.",
    genres: List<Genre> = listOf(Genre(1L, "Drama"), Genre(2L, "Fantasy")),
    headerPictures: List<String> = listOf(
        "https://image.tmdb.org/t/p/w500/header1.jpg",
        "https://image.tmdb.org/t/p/w500/header2.jpg"
    ),
    userRating: Int? = 9
) = Episode(
    id = id,
    title = title,
    episodeNumber = episodeNumber,
    rating = rating,
    duration = duration,
    releasedDate = releasedDate,
    trailerUrl = trailerUrl,
    currentSeason = currentSeason,
    overview = overview,
    genres = genres,
    headerPictures = headerPictures,
    userRating = userRating
)
