package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.usecase.genre.GenreMock
import com.baghdad.entity.media.TvShow
import kotlinx.datetime.LocalDate

object TvShowMock {

    const val TV_SHOW_ID = 1L
    val TV_SHOW = TvShow(
        id = TV_SHOW_ID,
        title = "Sample TV Show",
        overview = "This is a sample TV show overview.",
        posterImageURL = "https://example.com/poster.jpg",
        genres = GenreMock.GENRES,
        averageRating = 8.5,
        userRating = 9,
        releaseDate = LocalDate(2002, 2, 22),
        trailerURL = "https://example.com/trailer.mp4",
        headerImagesURLs = listOf(
            "https://example.com/header1.jpg",
            "https://example.com/header2.jpg"
        ),
        numberOfSeasons = 8,
    )
    val TV_SHOWS = listOf(
        TV_SHOW,
        TV_SHOW.copy(id = 2, title = "Another TV Show"),
        TV_SHOW.copy(id = 3, title = "Third TV Show"),
        TV_SHOW.copy(id = 4, title = "Fourth TV Show")
    )

    val TV_SHOW_RESULT = PagedResult(
        data = TV_SHOWS,
        nextPage = 2,
        prevPage = null
    )
}