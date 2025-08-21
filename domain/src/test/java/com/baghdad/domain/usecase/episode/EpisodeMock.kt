package com.baghdad.domain.usecase.episode

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import kotlinx.datetime.LocalDate

object EpisodeMock {
    val EPISODE = Episode(
        id = 1L,
        title = "Sample Episode",
        overview = "Sample episode description",
        episodeNumber = 1,
        rating = 8.7,
        duration = "45",
        releasedDate = LocalDate(2023, 1, 1),
        trailerUrl = "https://example.com/trailer.mp4",
        currentSeason = 1,
        userRating = 9,
        genres = listOf(Genre(id = 1, "Drama"), Genre(id = 2, "Action")),
        headerPictures = listOf(
            "https://example.com/episode.jpg",
            "https://example.com/episode2.jpg"
        ),
    )
    val EPISODES = listOf(
        EPISODE,
        EPISODE.copy(id = 2, title = "Another Episode"),
        EPISODE.copy(id = 3, title = "Third Episode"),
        EPISODE.copy(id = 4, title = "Fourth Episode")
    )
}