package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import kotlinx.datetime.LocalDate

object MockTvShow {
    val TV_SHOW = TvShow(
        id = 1L,
        title = "Test TvShow 1",
        genres = listOf(Genre(16L, "Animation")),
        averageRating = 8.0,
        userRating = 7,
        releaseDate = LocalDate.parse("2002-02-22"),
        overview = "Test",
        posterImageURL = "/movie_poster_1.jpg",
        trailerURL = "test_trailer_url",
        headerImagesURLs = listOf("/header_image_1.jpg".repeat(2)),
        numberOfSeasons = 7,
    )

    val TV_SHOWS: List<TvShow> = listOf(
        TV_SHOW,
        TV_SHOW.copy(id = 2L, title = "Test TvShow 2"),
        TV_SHOW.copy(id = 2L, title = "Test TvShow 3", genres = listOf(Genre(18L, "Drama")))
    )
}